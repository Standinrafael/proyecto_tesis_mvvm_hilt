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
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.ui.routes.Routes
import com.example.proyecto_tesis.ui.theme.Purple40
import com.example.proyecto_tesis.ui.viewmodels.AuthViewModel
import com.example.proyecto_tesis.ui.viewmodels.FirestoreViewModel
import com.example.proyecto_tesis.utils.AuthRes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.input.ImeAction
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import coil.compose.rememberAsyncImagePainter
import com.example.proyecto_tesis.ui.viewmodels.ImageUtilsViewModel
import com.example.proyecto_tesis.ui.viewmodels.SteganographyViewModel
import com.example.proyecto_tesis.utils.ImageUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
@Composable
fun EncodeScreen() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val firestoreViewModel: FirestoreViewModel = hiltViewModel()
    val steganographyViewModel: SteganographyViewModel = hiltViewModel()
    val imageUtilsViewModel: ImageUtilsViewModel = hiltViewModel()
    val context = LocalContext.current

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

    val file = context.createImageFile()
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
                Text(text = "Galería")
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
                Text(text = "Cámara")
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
                Text(text = "Galería")
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
                Text(text = "Cámara")
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
    var characterCount by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }
    val imageEncodeUri by steganographyViewModel.imageEncodeUri.collectAsState()
    val message by steganographyViewModel.message.collectAsState()

    val writePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (!inputImage.isNullOrEmpty()) {
                steganographyViewModel.injectEncryption(
                    inputImage,
                    message,
                    outputImage,
                    context.contentResolver
                )
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
            if (characterCount < 70) {
                steganographyViewModel.setMessage(newMessage.take(70))
                characterCount = newMessage.length
            }
            if (characterCount == 70) {
                Toast.makeText(
                    context, "Se ha alcanzado el límite de 70 caracteres", Toast.LENGTH_SHORT
                ).show()
            }
        },
        label = {
            Text(
                "Ingrese su mensaje: Límite 70 caracteres",
                style = MaterialTheme.typography.bodySmall
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
        ),
    )
    Spacer(modifier = Modifier.height(15.dp))

   if(isLoading){
       MyLoader()
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
                    steganographyViewModel.injectEncryption(
                    inputImage,
                    message,
                    outputImage,
                    context.contentResolver
                )
                withContext(Dispatchers.Main) {
                    isLoading = false
                    Toast.makeText(
                        context,
                        "Se encriptó la imagen guardada en imágenes - external SD",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            isLoading=true
        } else {
            Toast.makeText(ctx, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }) {
        Text(text = "Convertir")
    }
    ShareImageButton(imageEncodeUri)
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun encodeButton2(
    inputImage: String?,
    steganographyViewModel: SteganographyViewModel,
    outputImage: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var characterCount by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }
    val imageEncodeUri by steganographyViewModel.imageEncodeUri.collectAsState()
    val message by steganographyViewModel.message.collectAsState()

    val writePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (!inputImage.isNullOrEmpty()) {
                steganographyViewModel.injectEncryption(
                    inputImage,
                    message,
                    outputImage,
                    context.contentResolver
                )
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
            if (characterCount < 70) {
                steganographyViewModel.setMessage(newMessage.take(70))
                characterCount = newMessage.length
            }
            if (characterCount == 70) {
                Toast.makeText(
                    context, "Se ha alcanzado el límite de 70 caracteres", Toast.LENGTH_SHORT
                ).show()
            }
        },
        label = {
            Text(
                "Ingrese su mensaje: Límite 70 caracteres",
                style = MaterialTheme.typography.bodySmall
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
        ),
    )
    Spacer(modifier = Modifier.height(15.dp))

   if(isLoading){
       MyLoader()
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
                                steganographyViewModel.injectEncryption(
                                inputImage,
                                message,
                                outputImage,
                                context.contentResolver
                            )
                            withContext(Dispatchers.Main) {
                                isLoading = false
                                Toast.makeText(
                                    context,
                                    "Se encriptó la imagen guardada en imágenes - external SD",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    else -> {
                        writePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
                isLoading=true
            }

            else -> {
                readPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }) {
        Text(text = "Convertir2")
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
    var isLoading by remember { mutableStateOf(false) }
    var rememberCheckBoxState by remember { mutableStateOf(false) }

    LaunchedEffect(uriToImage2) {

        rememberCheckBoxState = loadCheckboxState(context)
    }

    if (isLoading ){
        MyLoader()
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
        Text("Compartir Imagen")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Aviso de seguridad de mensajes en aplicaciones de Meta") },
            text = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Si se envían mensajes por aplicaciones de Meta (WhatsApp, Messenger, Instagram), " +
                                "no se podrán desencriptar los mensajes."
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = rememberCheckBoxState,
                            onCheckedChange = { rememberCheckBoxState = it }
                        )
                        Text("Mostrar este diálogo la próxima vez")
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
                    Text("Confirmar")
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

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
}

