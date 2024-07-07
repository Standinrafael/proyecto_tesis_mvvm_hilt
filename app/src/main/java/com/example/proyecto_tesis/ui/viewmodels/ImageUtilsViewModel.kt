package com.example.proyecto_tesis.ui.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_tesis.logic.usercases.ImageUtilsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
@HiltViewModel
class ImageUtilsViewModel @Inject constructor(
    private val imageUtilsUseCase: ImageUtilsUseCase
) : ViewModel() {

    private val _compressedImageUri = MutableStateFlow<Uri?>(null)
    val compressedImageUri: StateFlow<Uri?> get() = _compressedImageUri

    private val _imageFile = MutableStateFlow<File?>(null)
    val imageFile: StateFlow<File?> get() = _imageFile

    fun compressImage(context: Context, uri: Uri): Uri? {
        val result = CompletableDeferred<Uri?>()
        viewModelScope.launch(Dispatchers.IO) {
            val compressedUri = imageUtilsUseCase.compressImage(context, uri)
            _compressedImageUri.value = compressedUri
            result.complete(compressedUri)
        }
        return runBlocking { result.await() }
    }

    fun createImageFile(context: Context): File {
        return imageUtilsUseCase.createImageFile(context)
    }


    fun getFileName(context: Context, uri: Uri?): String? {
        return imageUtilsUseCase.getFileName(context, uri)
    }
}