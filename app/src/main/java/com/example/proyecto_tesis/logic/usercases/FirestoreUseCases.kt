package com.example.proyecto_tesis.logic.usercases

import com.example.proyecto_tesis.data.repository.FirestoreRepository
import javax.inject.Inject

class FirestoreUseCases @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend fun getNextIdFromFirestore(): Int {
        return firestoreRepository.getNextIdFromFirestore()
    }

    suspend fun userId(authUID: String?): Int {
        return firestoreRepository.userId(authUID)
    }

    suspend fun deleteUserByAuthUID(authUID: String): Boolean {
        return firestoreRepository.deleteUserByAuthUID(authUID)
    }

    suspend fun userPassword(id: Int?): String {
        return firestoreRepository.userPassword(id)
    }
}