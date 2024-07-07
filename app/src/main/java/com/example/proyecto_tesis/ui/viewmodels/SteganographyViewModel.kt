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
    ): Uri? {
        _isEncrypting.value = true
        val uri = withContext(Dispatchers.IO) {
            val userId = _userId.value ?: return@withContext null
            val password = _password.value ?: return@withContext null
            steganographyUseCase.injectEncryption(
                inputImage,
                message,
                userId,
                password,
                outputImage,
                contentResolver
            )
        }
        _isEncrypting.value = false
        _imageEncodeUri.value = uri
        return uri
    }

    fun extractEncrypt(inputImageName: String, contentResolver: ContentResolver) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = steganographyUseCase.extractEncrypt(inputImageName, contentResolver)
            _extractedMessage.value = result
        }
    }

    fun clearMessageField() {
        _extractedMessage.value = ""
    }


    fun setMessage(newMessage: String) {
        _message.value = newMessage
    }
}
