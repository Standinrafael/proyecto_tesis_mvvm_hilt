package com.example.proyecto_tesis.logic.usercases

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.proyecto_tesis.data.repository.FirestoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SteganographyUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val encryptionUseCase: EncryptionUseCase
) {
    suspend fun hideMessageInImage(
        addedImage: Bitmap,
        encryptBytes: ByteArray
    ): Boolean = withContext(Dispatchers.IO) {
        var addedBytes = ""
        var index = 0
        var bits = 7

        for (y in 0 until addedImage.height) {
            for (x in 0 until addedImage.width) {
                val pixel = addedImage.getPixel(x, y)
                val red = Color.red(pixel)
                val green = Color.green(pixel)
                var blue = Color.blue(pixel)

                var bit = blue and 1
                if (index <= encryptBytes.lastIndex) {
                    bit = (encryptBytes[index].toInt() shr bits) and 1
                    addedBytes += bit.toString()
                    bits--
                }
                blue = blue and 0xFE or bit

                val newColor = Color.rgb(red, green, blue)
                addedImage.setPixel(x, y, newColor)

                if (bits == -1) {
                    bits = 7
                    addedBytes = ""
                    index++
                }
            }
        }
        return@withContext true
    }

    suspend fun injectEncryption(
        addedImage: String,
        message: String,
        userId: Int,
        password: String,
        outputImage: String,
        contentResolver: ContentResolver
    ): Uri? = withContext(Dispatchers.IO) {
        val imageDirection = Uri.parse(addedImage)
        val inputStream = contentResolver.openInputStream(imageDirection)
        if (inputStream == null) {
            return@withContext null
        }

        val bytesMensajeEncriptado = encryptionUseCase.encrypt(message, password, userId)
        val originalImage = BitmapFactory.decodeStream(inputStream)
        val image = originalImage.copy(originalImage.config, true)

        if (image == null) {
            return@withContext null
        }
        if (image.width * image.height < bytesMensajeEncriptado.size * 8) {
            return@withContext null
        }

        if (!hideMessageInImage(image, bytesMensajeEncriptado)) {
            return@withContext null
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, outputImage)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        val imageUri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        imageUri?.let { uri ->
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                image.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
                return@withContext uri
            }
        }
        true
    } as Uri?

    suspend fun extractEncrypt(
        inputImageName: String,
        contentResolver: ContentResolver
    ): String = withContext(Dispatchers.IO) {
        val imageDirection = Uri.parse(inputImageName)
        val inputStream = contentResolver.openInputStream(imageDirection)
        var messageFound = false
        if (inputStream == null) {
            return@withContext "false"
        }

        val messageAsBytes = mutableListOf<Byte>()
        val bits = mutableListOf<Int>()
        val originalImage = BitmapFactory.decodeStream(inputStream)
        val inputImage = originalImage.copy(originalImage.config, true)
        if (inputImage == null) {
            return@withContext "false"
        }

        extractMessage@ for (y in 0 until inputImage.height) {
            for (x in 0 until inputImage.width) {
                val pixel = inputImage.getPixel(x, y)
                val blue = pixel and 0x0000FF

                bits.add(blue and 1)
                if (bits.size == 8) {
                    bits.reverse()
                    var value = 0
                    bits.forEachIndexed { index, i ->
                        value += i shl index
                    }
                    messageAsBytes.add(value.toByte())
                    bits.clear()
                }

                if (messageAsBytes.size > 3 && messageAsBytes.last().toInt() == -1) {
                    messageFound = true
                    break@extractMessage
                }
            }
        }
        if (messageFound) {
            encryptionUseCase.decrypt(messageAsBytes)
        } else {
            "false"
        }
    }
}