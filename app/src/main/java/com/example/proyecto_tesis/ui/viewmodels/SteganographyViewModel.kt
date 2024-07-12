package com.example.proyecto_tesis.ui.viewmodels

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_tesis.logic.usercases.AuthUseCases
import com.example.proyecto_tesis.logic.usercases.EncryptionUseCase
import com.example.proyecto_tesis.logic.usercases.FirestoreUseCases
import com.example.proyecto_tesis.logic.usercases.SteganographyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class SteganographyViewModel @Inject constructor(
    private val steganographyUseCase: SteganographyUseCase,
    private val encryptionUseCase: EncryptionUseCase
) : ViewModel() {

    private val _hiddenMessageResult = MutableStateFlow<Uri?>(null)
    val hiddenMessageResult: StateFlow<Uri?> get() = _hiddenMessageResult

    private val _extractedMessage = MutableStateFlow<String?>(null)
    val extractedMessage: StateFlow<String?> get() = _extractedMessage

    private val _imageEncodeUri = MutableStateFlow<Uri?>(null)
    val imageEncodeUri: StateFlow<Uri?> get() = _imageEncodeUri

    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> get() = _userId

    private val _password = MutableStateFlow<String?>(null)
    val password: StateFlow<String?> get() = _password

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> get() = _message

    private val _isEncrypting = MutableStateFlow(false)
    val isEncrypting: StateFlow<Boolean> get() = _isEncrypting


    private val _characterCount = MutableStateFlow(0)
    val characterCount: StateFlow<Int> get() = _characterCount

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> get() = _toastMessage


    fun hideMessageInImage(addedImage: Bitmap, encryptBytes: ByteArray) {
        viewModelScope.launch(Dispatchers.IO) {
            steganographyUseCase.hideMessageInImage(addedImage, encryptBytes)
        }
    }

    fun fetchUserData(authViewModel: AuthViewModel, firestoreViewModel: FirestoreViewModel) {
        viewModelScope.launch {
            val currentUserUid = authViewModel.getCurrentUid()
            if (currentUserUid != null) {
                val fetchedUserId = firestoreViewModel.fetchUserId(currentUserUid)
                val fetchedPassword = firestoreViewModel.fetchUserPassword(fetchedUserId)
                _userId.value = fetchedUserId
                _password.value = fetchedPassword
            }
        }
    }
    suspend fun injectEncryption(
        inputImage: String,
        message: String,
        outputImage: String,
        contentResolver: ContentResolver
    ): Uri? = withContext(Dispatchers.IO) {
        _isEncrypting.value = true
        val uri = steganographyUseCase.injectEncryption(
            inputImage,
            message,
            userId.value ?: return@withContext null,
            password.value ?: return@withContext null,
            outputImage,
            contentResolver
        )
        _isEncrypting.value = false
        _imageEncodeUri.value = uri
        return@withContext uri
    }


    fun extractEncrypt(inputImage: String, contentResolver: ContentResolver) {
        viewModelScope.launch {
            val result = steganographyUseCase.extractEncrypt(inputImage, contentResolver)
            _extractedMessage.value = when (result) {
                "false" -> {
                    _toastMessage.value = "No se encontró un mensaje oculto en la imagen"
                    null
                }
                "Usuario no encontrado" -> {
                    _toastMessage.value = "Usuario ya no existe. No se puede desencriptar el mensaje"
                    null
                }
                else -> result
            }
        }
    }

    fun clearMessageField() {
        _extractedMessage.value = ""
    }


    fun setMessage(newMessage: String) {
        _message.value = newMessage
    }

    fun updateMessage(newMessage: String) {
        if (newMessage.length <= 70) {
            _message.value = newMessage
            _characterCount.value = newMessage.length
        }
        if (newMessage.length == 70) {
            _toastMessage.value = "Se ha alcanzado el límite de 70 caracteres"
        } else {
            _toastMessage.value = null
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }
}
