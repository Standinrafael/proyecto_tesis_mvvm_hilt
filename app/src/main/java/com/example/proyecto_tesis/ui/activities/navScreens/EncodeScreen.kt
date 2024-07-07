package com.example.proyecto_tesis.ui.activities.navScreens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyecto_tesis.ui.viewmodels.AuthViewModel
import com.example.proyecto_tesis.ui.viewmodels.FirestoreViewModel
import androidx.compose.ui.text.input.ImeAction
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.ui.viewmodels.ImageUtilsViewModel
import com.example.proyecto_tesis.ui.viewmodels.SteganographyViewModel
import com.example.proyecto_tesis.utils.ImageUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

@Composable
fun EncodeScreen() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val firestoreViewModel: FirestoreViewModel = hiltViewModel()
    val steganographyViewModel: SteganographyViewModel = hiltViewModel()
    val imageUtilsViewModel: ImageUtilsViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        steganographyViewModel.fetchUserData(authViewModel, firestoreViewModel)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Seleccione la imagen",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PickImageFromGallery(steganographyViewModel, imageUtilsViewModel)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            PickImageFromGallery2(steganographyViewModel, imageUtilsViewModel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PickImageFromGallery(
    steganographyViewModel: SteganographyViewModel,
    imageUtilsViewModel: ImageUtilsViewModel
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            steganographyViewModel.clearMessageField()
        }

    val file = imageUtilsViewModel.createImageFile(context)
    val uri = FileProvider.getUriForFile(
        context, context.packageName + ".provider", file
    )
    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) {
            imageUri = uri
            steganographyViewModel.clearMessageField()
        }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(5.dp)
                .border(BorderStroke(2.dp, Color.DarkGray))
        ) {
            imageUri?.let {
                if (imageUri!!.path?.isNotEmpty() == true) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = null
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row {
            Button(onClick = { launcher.launch("image/*") }) {
                Text(text = stringResource(id = R.string.galery))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Text(text = stringResource(id = R.string.camera))
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        encodeButton(imageUri?.toString(), steganographyViewModel, "mensaje.jpg")
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun PickImageFromGallery2(
    steganographyViewModel: SteganographyViewModel,
    imageUtilsViewModel: ImageUtilsViewModel
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var compressedImageUri by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            uri?.let {
                coroutineScope.launch {
                    compressedImageUri = imageUtilsViewModel.compressImage(context, it)
                }
            }
            steganographyViewModel.clearMessageField()
        }

    val file = ImageUtils.createImageFile(context)
    val uri = FileProvider.getUriForFile(
        context, context.packageName + ".provider", file
    )
    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) {
            imageUri = uri
            uri?.let {
                coroutineScope.launch {
                    compressedImageUri = imageUtilsViewModel.compressImage(context, it)
                }
            }
            steganographyViewModel.clearMessageField()
        }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(5.dp)
                .border(BorderStroke(2.dp, Color.DarkGray))
        ) {
            imageUri?.let {
                if (imageUri!!.path?.isNotEmpty() == true) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = null
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row {
            Button(onClick = { launcher.launch("image/*") }) {
                Text(text = stringResource(id = R.string.galery))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Text(text = stringResource(id = R.string.camera))
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        encodeButton2(
            compressedImageUri?.toString(),
            steganographyViewModel,
            imageUtilsViewModel.getFileName(context, compressedImageUri).toString()
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun encodeButton(
    inputImage: String?,
    steganographyViewModel: SteganographyViewModel,
    outputImage: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isLoading by steganographyViewModel.isEncrypting.collectAsState()
    val imageEncodeUri by steganographyViewModel.imageEncodeUri.collectAsState()
    val message by steganographyViewModel.message.collectAsState()
    val toastMessage by steganographyViewModel.toastMessage.collectAsState()

    val writePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (!inputImage.isNullOrEmpty()) {
                scope.launch {
                    val uri = steganographyViewModel.injectEncryption(
                        inputImage,
                        message,
                        outputImage,
                        context.contentResolver
                    )
                    if (uri != null) {
                        Toast.makeText(
                            context,
                            "Se encriptó la imagen guardada en imágenes - external SD",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Error al encriptar la imagen",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
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

    OutlinedTextField(
        value = message,
        onValueChange = { newMessage ->
            steganographyViewModel.updateMessage(newMessage)
        },
        label = {
            Text(
                stringResource(id = R.string.limited_caracter),
                style = MaterialTheme.typography.bodySmall
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
        ),
    )
    Spacer(modifier = Modifier.height(15.dp))

    if (isLoading) {
        MyLoader()
    }

    toastMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        steganographyViewModel.clearToastMessage()
    }

    val permission = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )
    val ctx = LocalContext.current

    Button(onClick = {
        permission.launchMultiplePermissionRequest()

        val p = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            permission.permissions[0].status.isGranted
        } else {
            permission.permissions.all {
                it.status.isGranted
            }
        }

        if (p) {
            if (inputImage.isNullOrEmpty()) {
                Toast.makeText(
                    context, "Debe seleccionar una imagen primero", Toast.LENGTH_SHORT
                ).show()
                return@Button
            }

            scope.launch {
                val uri = steganographyViewModel.injectEncryption(
                    inputImage,
                    message,
                    outputImage,
                    context.contentResolver
                )
                if (uri != null) {
                    Toast.makeText(
                        context,
                        "Se encriptó la imagen guardada en imágenes - external SD",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.i("infocompresed", uri.toString())
                } else {
                    Toast.makeText(
                        context,
                        "Error al encriptar la imagen",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(ctx, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }) {
        Text(text = stringResource(id = R.string.converter))
    }
    ShareImageButton(imageEncodeUri)
}


@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun encodeButton2(
    inputImage: String?,
    steganographyViewModel: SteganographyViewModel,
    outputImage: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isLoading by steganographyViewModel.isEncrypting.collectAsState()
    val imageEncodeUri by steganographyViewModel.imageEncodeUri.collectAsState()
    val message by steganographyViewModel.message.collectAsState()
    val toastMessage by steganographyViewModel.toastMessage.collectAsState()

    val writePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (!inputImage.isNullOrEmpty()) {
                scope.launch {
                    val uri = steganographyViewModel.injectEncryption(
                        inputImage,
                        message,
                        outputImage,
                        context.contentResolver
                    )
                    if (uri != null) {
                        Toast.makeText(
                            context,
                            "Se encriptó la imagen guardada en imágenes - external SD",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.i("infocompresed", uri.toString())
                    } else {
                        Toast.makeText(
                            context,
                            "Error al encriptar la imagen",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
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

    OutlinedTextField(
        value = message,
        onValueChange = { newMessage ->
            steganographyViewModel.updateMessage(newMessage)
        },
        label = {
            Text(
                stringResource(id = R.string.limited_caracter),
                style = MaterialTheme.typography.bodySmall
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
        ),
    )
    Spacer(modifier = Modifier.height(15.dp))

    if (isLoading) {
        MyLoader()
    }

    toastMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        steganographyViewModel.clearToastMessage()
    }

    Button(onClick = {
        if (inputImage.isNullOrEmpty()) {
            Toast.makeText(context, "Debe seleccionar una imagen primero", Toast.LENGTH_SHORT)
                .show()
            return@Button
        }
        when {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                when {
                    ContextCompat.checkSelfPermission(
                        context, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        scope.launch {
                            val uri = steganographyViewModel.injectEncryption(
                                inputImage,
                                message,
                                outputImage,
                                context.contentResolver
                            )
                            if (uri != null) {

                                Toast.makeText(
                                    context,
                                    "Se encriptó la imagen guardada en imágenes - external SD",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error al encriptar la imagen",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    else -> {
                        writePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
            }

            else -> {
                readPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }) {
        Text(text = stringResource(id = R.string.converter))
    }
    ShareImageButton(imageEncodeUri)
}


@Composable
fun MyLoader() {
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ShareImageButton(uriToImage2: Uri?) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var rememberCheckBoxState by remember { mutableStateOf(false) }

    LaunchedEffect(uriToImage2) {
        rememberCheckBoxState = loadCheckboxState(context)
    }

    Button(
        enabled = uriToImage2 != null,
        onClick = {
            if (rememberCheckBoxState) {
                shareImage(context, uriToImage2)
            } else {
                showDialog = true
            }
        }
    )
    {
        Text(stringResource(id = R.string.share_image))
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(id = R.string.title_dialog_message)) },
            text = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(id = R.string.dialog_message_meta_applications)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = rememberCheckBoxState,
                            onCheckedChange = { rememberCheckBoxState = it }
                        )
                        Text(stringResource(id = R.string.dialog_message))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        saveCheckboxState(context, rememberCheckBoxState)
                        shareImage(context, uriToImage2)
                    }
                ) {
                    Text(stringResource(id = R.string.accept_dialog))
                }
            }
        )
    }
}

fun shareImage(context: Context, uriToImage2: Uri?) {
    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TITLE, "No se puede comprimir una imagen")
        putExtra(
            Intent.EXTRA_STREAM, uriToImage2
        )
        putExtra(Intent.EXTRA_TEXT, "Imagen")
        type = "application/pdf"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    val chooserTitle = "Elige una aplicación para compartir"
    val chooser = Intent.createChooser(shareIntent, chooserTitle)
    context.startActivity(chooser)
}

fun saveCheckboxState(context: Context, state: Boolean) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    preferences.edit().putBoolean("checkbox_state", state).apply()
}

fun loadCheckboxState(context: Context): Boolean {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    return preferences.getBoolean(
        "checkbox_state", false
    )
}



