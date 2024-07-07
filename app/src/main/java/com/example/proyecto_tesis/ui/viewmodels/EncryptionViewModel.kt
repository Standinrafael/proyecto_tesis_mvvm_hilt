package com.example.proyecto_tesis.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_tesis.logic.usercases.EncryptionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EncryptionViewModel @Inject constructor(
    private val encryptionUseCase: EncryptionUseCase
) : ViewModel() {

    private val _encryptedData = MutableStateFlow<ByteArray?>(null)
    val encryptedData: StateFlow<ByteArray?> get() = _encryptedData

    private val _decryptedMessage = MutableStateFlow<String?>(null)
    val decryptedMessage: StateFlow<String?> get() = _decryptedMessage

    fun encrypt(message: String, password: String, idUser: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val encryptedBytes = encryptionUseCase.encrypt(message, password, idUser)
            _encryptedData.value = encryptedBytes
        }
    }

    fun decrypt(encryptedList: MutableList<Byte>) {
        viewModelScope.launch(Dispatchers.IO) {
            val decryptedMessage = encryptionUseCase.decrypt(encryptedList)
            _decryptedMessage.value = decryptedMessage
        }
    }
}