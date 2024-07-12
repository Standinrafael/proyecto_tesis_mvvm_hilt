package com.example.proyecto_tesis.logic.usercases

import android.util.Log
import com.example.proyecto_tesis.data.repository.FirestoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.spec.AlgorithmParameterSpec
import javax.inject.Inject
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend fun encrypt(
        message: String,
        password: String,
        idUser: Int
    ): ByteArray = withContext(Dispatchers.IO) {
        var result: ByteArray? = null

        try {
            val randomNumber = (1 until message.length).random()
            val encryptedBytes = encryptText(password, message)
            val (prefix, suffix) = encryptedBytes!!.take(randomNumber) to encryptedBytes.drop(
                randomNumber
            )
            val encryptedBytes2 = prefix.toByteArray() + idUser.toByte() + suffix.toByteArray()

            val encryptedList = mutableListOf<Byte>()
            encryptedList.add(randomNumber.toByte())
            encryptedList.addAll(encryptedBytes2.toList())
            encryptedList.add(-1)

            result = encryptedList.toByteArray()
        } catch (e: Exception) {
            result = ByteArray(0)
        }

        return@withContext result!!
    }

    private fun encryptText(password: String, message: String): ByteArray? {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val key = SecretKeySpec(password.toByteArray(), "AES")
        val iv: AlgorithmParameterSpec = IvParameterSpec(ByteArray(16))
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        return cipher.doFinal(message.toByteArray())
    }

    suspend fun decrypt(
        encryptedList: MutableList<Byte>
    ): String = withContext(Dispatchers.IO) {
        if (encryptedList.isEmpty()) {
            return@withContext "false"
        }
        val random = encryptedList[0].toInt()

        if (random < 0 || random >= encryptedList.size - 1) {
            return@withContext "false"
        }
        val idUserPos = encryptedList.getOrNull(random + 1)?.toInt()
            ?: return@withContext "Error: Valor idUserPos fuera de rango"

        encryptedList.removeAt(0)
        encryptedList.removeAt(random)

        val passwordUserDecrypt = firestoreRepository.userPassword(idUserPos)

        if (passwordUserDecrypt == "0000000000000000") {
            return@withContext "Usuario no encontrado"
        } else {
            encryptedList.removeAt(encryptedList.size - 1)
            var decryptedMessage = ""

            try {
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                val key = SecretKeySpec(passwordUserDecrypt.toByteArray(), "AES")
                val iv = IvParameterSpec(ByteArray(16))
                cipher.init(Cipher.DECRYPT_MODE, key, iv)
                val decryptedBytes = cipher.doFinal(encryptedList.toByteArray())

                val decryptedList = mutableListOf<Byte>()
                decryptedList.addAll(decryptedBytes.toList())

                decryptedMessage = decryptedList.toByteArray().toString(Charsets.UTF_8)
            } catch (e: Exception) {
                decryptedMessage = "Error al desencriptar: ${e.localizedMessage}"
            }

            return@withContext decryptedMessage
        }
    }
}