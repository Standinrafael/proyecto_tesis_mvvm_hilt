package com.example.proyecto_tesis.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) {

    suspend fun getNextIdFromFirestore(): Int {
        val querySnapshot = firestore.collection("users")
            .orderBy("id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        return if (querySnapshot.isEmpty) {
            1
        } else {
            val lastDocument = querySnapshot.documents.first()
            val lastId = lastDocument.getLong("id")?.toInt() ?: 0
            lastId + 1
        }
    }

    suspend fun userId(authUID: String?): Int {
        return try {
            val querySnapshot = firestore.collection("users")
                .whereEqualTo("user_id", authUID)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                throw RuntimeException("Usuario no encontrado")
            } else {
                val document = querySnapshot.documents.firstOrNull()
                requireNotNull(document).getLong("id")!!.toInt()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun deleteUserByAuthUID(authUID: String): Boolean {
        return try {
            val querySnapshot = firestore.collection("users")
                .whereEqualTo("user_id", authUID)
                .limit(1)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val document = querySnapshot.documents[0]
                document.reference.delete().await()
            }
            true
        } catch (e: Exception) {
            Log.e("TAG", "Error al eliminar usuario por authUID: $e")
            false
        }
    }

    suspend fun userPassword(id: Int?): String {
        return try {
            val querySnapshot = firestore.collection("users")
                .whereEqualTo("id", id)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                "0000000000000000"
            } else {
                val document = querySnapshot.documents.firstOrNull()
                requireNotNull(document).getString("uniquePassword")!!
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
