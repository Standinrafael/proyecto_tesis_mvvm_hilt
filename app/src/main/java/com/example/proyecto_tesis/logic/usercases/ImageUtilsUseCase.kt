package com.example.proyecto_tesis.logic.usercases

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.preference.PreferenceManager
import android.provider.OpenableColumns
import com.example.proyecto_tesis.utils.ImageUtils
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class ImageUtilsUseCase @Inject constructor() {

    fun compressImage(context: Context, uri: Uri): Uri {
        return ImageUtils.compressImage(context, uri)
    }

    fun createImageFile(context: Context): File {
        return ImageUtils.createImageFile(context)
    }

    fun getFileName(context: Context, uri: Uri?): String? {
        return ImageUtils.getFileName(context, uri)
    }
}