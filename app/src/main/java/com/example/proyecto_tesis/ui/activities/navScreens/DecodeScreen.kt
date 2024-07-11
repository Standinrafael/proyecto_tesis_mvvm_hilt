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
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.ui.theme.color_azul
import com.example.proyecto_tesis.ui.theme.color_barra
import com.example.proyecto_tesis.ui.theme.color_blanco
import com.example.proyecto_tesis.ui.theme.color_verde
import com.example.proyecto_tesis.ui.viewmodels.ImageUtilsViewModel
import com.example.proyecto_tesis.ui.viewmodels.SteganographyViewModel
import com.example.proyecto_tesis.utils.monserratRegular
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
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(
                fontFamily = monserratRegular,
                color = color_blanco
            )
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
    val messageState by steganographyViewModel.extractedMessage.collectAsState()

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
        Box(
            modifier = Modifier
                .size(300.dp)
                .padding(5.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(size = 20.dp))
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(size = 20.dp),
                    color = color_azul
                )
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

        Row(modifier = Modifier.padding(10.dp, 0.dp)) {
            Button(
                onClick = { launcher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(containerColor = color_azul),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.galeria),
                    contentDescription = "encode image",
                    tint = color_verde,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                //DecodificarButton(imageUri?.toString(), steganographyViewModel)
                val writePermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted) {
                        if (!imageUri?.toString().isNullOrEmpty()) {
                            steganographyViewModel.extractEncrypt(
                                imageUri!!.toString(),
                                context.contentResolver
                            )
                        } else {
                            Toast.makeText(
                                context,
                                "Debe seleccionar una imagen primero",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } else {
                        Toast.makeText(context, "Permiso de escritura denegado", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                val readPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted) {
                        writePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    } else {
                        Toast.makeText(context, "Permiso de lectura denegado", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                Button(
                    onClick = {
                        readPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = color_verde),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.candado_abierto),
                        contentDescription = "encode image",
                        tint = color_azul,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(stringResource(id = R.string.decode), color = color_azul)
                }


            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
                val writePermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted) {
                        if (!imageUri?.toString().isNullOrEmpty()) {
                            steganographyViewModel.extractEncrypt(
                                imageUri!!.toString(),
                                context.contentResolver
                            )
                        } else {
                            Toast.makeText(
                                context,
                                "Debe seleccionar una imagen primero",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Permiso de escritura denegado",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                val readPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted) {
                        writePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    } else {
                        Toast.makeText(
                            context,
                            "Permiso de lectura denegado",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                Button(
                    onClick = {
                        readPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = color_verde),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.candado_abierto),
                        contentDescription = "encode image",
                        tint = color_azul,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(stringResource(id = R.string.decode), color = color_azul)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = messageState ?: "",
            modifier = Modifier
                .padding(15.dp)
                .background(color = color_blanco)
                .fillMaxSize(),
        )
    }
}
