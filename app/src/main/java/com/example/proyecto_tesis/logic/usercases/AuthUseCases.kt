package com.example.proyecto_tesis.logic.usercases

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.proyecto_tesis.data.repository.AuthRepository
import com.example.proyecto_tesis.utils.AuthRes
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import javax.inject.Inject


class AuthUseCases @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun createUserWithEmailandPassword(email: String, password: String): AuthRes<FirebaseUser?> {
        return authRepository.createUserWithEmailandPassword(email, password)
    }

    suspend fun signInWithEmailandPassword(email: String, password: String): AuthRes<FirebaseUser?> {
        return authRepository.signInWithEmailandPassword(email, password)
    }

    fun getCurrentUser(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }

    suspend fun deleteCurrentUser(): Boolean {
        return authRepository.deleteCurrentUser()
    }

    fun getCurrentUid(): String? {
        return authRepository.getCurrentUid()
    }

    fun signOut() {
        authRepository.signOut()
    }

    suspend fun resetPassword(email: String): AuthRes<Unit> {
        return authRepository.resetPassword(email)
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>): AuthRes<GoogleSignInAccount>? {
        return authRepository.handleSignInResult(task)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun signInWithGoogleCredential(credential: AuthCredential): AuthRes<FirebaseUser> {
        return authRepository.signInWithGoogleCredential(credential)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createUser(): AuthRes<DocumentReference> {
        return authRepository.createUser()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createGoogleUser(firebaseUser: FirebaseUser): AuthRes<DocumentReference> {
        return authRepository.createGoogleUser(firebaseUser)
    }
}
