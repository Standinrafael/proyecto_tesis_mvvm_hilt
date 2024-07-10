package com.example.proyecto_tesis.ui.activities.navScreens

import android.Manifest
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.ui.viewmodels.ImageUtilsViewModel
import com.example.proyecto_tesis.ui.viewmodels.SteganographyViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@Composable
fun DecodeScreen() {
    val steganographyViewModel: SteganographyViewModel = hiltViewModel()
    val imageUtilsViewModel: ImageUtilsViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.choose_image),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally).
            padding(0.dp,10.dp,0.dp,0.dp)
        )
        PickImageFromGalleryDeco(steganographyViewModel, imageUtilsViewModel)
    }
}

@Composable
fun PickImageFromGalleryDeco(
    steganographyViewModel: SteganographyViewModel,
    imageUtilsViewModel: ImageUtilsViewModel
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            steganographyViewModel.clearMessageField()
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = stringResource(id = R.string.galery))
        }
        Box(
            modifier = Modifier
                .size(250.dp)
                .padding(5.dp)
                .border(BorderStroke(2.dp, Color.DarkGray))
        ) {
            imageUri?.let {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = MediaStore.Images
                        .Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            DecodificarButton(imageUri?.toString(), steganographyViewModel)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            DecodificarButtond(imageUri?.toString(), steganographyViewModel)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DecodificarButton(
    inputImage: String?,
    steganographyViewModel: SteganographyViewModel
) {
    val context = LocalContext.current
    val messageState by steganographyViewModel.extractedMessage.collectAsState()

    val writePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (!inputImage.isNullOrEmpty()) {
                steganographyViewModel.extractEncrypt(inputImage, context.contentResolver)
            } else {
                Toast.makeText(context, "Debe seleccionar una imagen primero", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(context, "Permiso de escritura denegado", Toast.LENGTH_SHORT).show()
        }
    }

    val readPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            writePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            Toast.makeText(context, "Permiso de lectura denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Button(onClick = {
        readPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }) {
        Text(stringResource(id = R.string.extract_message))
    }
    Spacer(modifier = Modifier.height(15.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            text = messageState ?: "",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun DecodificarButtond(
    inputImage: String?,
    steganographyViewModel: SteganographyViewModel
) {
    val context = LocalContext.current
    val messageState by steganographyViewModel.extractedMessage.collectAsState()

    val writePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (!inputImage.isNullOrEmpty()) {
                steganographyViewModel.extractEncrypt(inputImage, context.contentResolver)
            } else {
                Toast.makeText(context, "Debe seleccionar una imagen primero", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(context, "Permiso de escritura denegado", Toast.LENGTH_SHORT).show()
        }
    }

    val readPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            writePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            Toast.makeText(context, "Permiso de lectura denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Button(onClick = {
        readPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }) {
        Text(stringResource(id = R.string.extract_message))
    }
    Spacer(modifier = Modifier.height(15.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            text = messageState ?: "",
            modifier = Modifier.padding(16.dp)
        )
    }
}
