package com.example.proyecto_tesis.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.proyecto_tesis.data.entities.User
import com.example.proyecto_tesis.utils.AuthRes
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


// AuthRepository.kt
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,
    private val firestore: FirebaseFirestore,
    private val context: Context
) {

    suspend fun createUserWithEmailandPassword(email: String, password: String): AuthRes<FirebaseUser?> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            AuthRes.Success(authResult.user)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al crear el usuario")
        }
    }

    suspend fun signInWithEmailandPassword(email: String, password: String): AuthRes<FirebaseUser?> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthRes.Success(authResult.user)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    suspend fun deleteCurrentUser(): Boolean {
        val user = firebaseAuth.currentUser
        return try {
            user?.delete()?.await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getCurrentUid(): String? {
        return firebaseAuth.currentUser?.uid
    }

    fun signOut() {
        firebaseAuth.signOut()
        googleSignInClient.signOut()
    }

    suspend fun resetPassword(email: String): AuthRes<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            AuthRes.Success(Unit)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al restablecer la contraseña")
        }
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>): AuthRes<GoogleSignInAccount>? {
        return try {
            val account = task.getResult(ApiException::class.java)
            AuthRes.Success(account)
        } catch (e: ApiException) {
            AuthRes.Error(e.message ?: "Registro con Google ha fallado")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun signInWithGoogleCredential(credential: AuthCredential): AuthRes<FirebaseUser> {
        return try {
            val firebaseUser = firebaseAuth.signInWithCredential(credential).await()
            firebaseUser.user?.let {
                val userId = it.uid
                val querySnapshot = firestore.collection("users")
                    .whereEqualTo("user_id", userId)
                    .get()
                    .await()

                if (!querySnapshot.isEmpty) {
                    AuthRes.Success(it)
                } else {
                    createGoogleUser(it)
                    AuthRes.Success(it)
                }
            } ?: throw Exception("Inicio de sesión con Google fallida")
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Inicio de sesión con Google fallida")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createUser(): AuthRes<DocumentReference> {
        return try {
            val user = firebaseAuth.currentUser
            val userAdd = User(
                id = getNextIdFromFirestore(),
                userId = user?.uid.toString(),
                uniquePassword = obtainDate()
            ).toMap()

            AuthRes.Success(firestore.collection("users").add(userAdd).await())
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al ingresar usuario sesión")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createGoogleUser(firebaseUser: FirebaseUser): AuthRes<DocumentReference> {
        return try {
            val user = User(
                id = getNextIdFromFirestore(),
                userId = firebaseUser.uid.toString(),
                uniquePassword = obtainDate()
            ).toMap()

            AuthRes.Success(firestore.collection("users").add(user).await())
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al ingresar usuario sesión")
        }
    }

    private suspend fun getNextIdFromFirestore(): Int {

        val querySnapshot = firestore.collection("users")
            .orderBy("id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        return if (querySnapshot.isEmpty) {

            // Si no hay documentos, el primero se crea con id 1
            1
        } else {

            // Si ya existe valores altos, se coge el mas alto y se suma 1 la id del nuevo usuario
            val lastDocument = querySnapshot.documents.first()
            val lastId = lastDocument.getLong("id")?.toInt() ?: 0
            lastId + 1
        }
    }

    private fun obtainDate(): String {
        // Implementación para obtener la fecha
        //Variable para obtener la fecha actual
        val currentDateTime = LocalDateTime.now()

        //Envair el tipo de formato: 16 caracteres para usarlo de contrasenia
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS")

        return currentDateTime.format(formatter)
    }
}
