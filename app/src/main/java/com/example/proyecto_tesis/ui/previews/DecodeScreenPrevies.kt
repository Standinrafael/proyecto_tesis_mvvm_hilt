package com.example.proyecto_tesis.ui.previews

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.ui.theme.Proyecto_tesisTheme
import com.example.proyecto_tesis.utils.monserratLight
import com.example.proyecto_tesis.utils.monserratRegular

@Preview(showSystemUi = true)
@Composable
fun PreviewDecodeScreen() {
    Proyecto_tesisTheme {
        DecodeScreenPreview()
    }
}

@Composable
fun DecodeScreenPreview() {
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
                color = Color(0xFFFFFFFF)
            )
        )
        PickImageFromGalleryDecoPreview()
    }
}

@Composable
fun PickImageFromGalleryDecoPreview() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val messageState = remember { mutableStateOf("Mensaje decodificado de ejemplo") }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
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
                    color = Color(0xFF125E73)
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF125E73)),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.recurso_13),
                    contentDescription = "encode image",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Button(
                    onClick = { /* Acción de decodificación */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF93C464)),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.recurso_22),
                        contentDescription = "encode image",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(stringResource(id = R.string.decode))
                }

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
                Button(
                    onClick = { /* Acción de decodificación */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF93C464)),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.recurso_22),
                        contentDescription = "encode image",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(stringResource(id = R.string.decode))
                }

            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = messageState.value,
            modifier = Modifier.padding(15.dp)
        )
    }

}