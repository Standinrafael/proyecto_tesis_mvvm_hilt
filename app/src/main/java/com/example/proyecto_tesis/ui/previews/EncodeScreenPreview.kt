package com.example.proyecto_tesis.ui.previews

import android.net.Uri
import android.os.Build
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.ui.theme.Proyecto_tesisTheme
import com.example.proyecto_tesis.utils.monserratMedium
import com.example.proyecto_tesis.utils.monserratRegular
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEncodeScreen() {
    Proyecto_tesisTheme {
        EncodeScreen()
    }
}

@Composable
fun EncodeScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Seleccione la imagen",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = monserratRegular, color = Color(0xFFFFFFFF)
            )
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            PickImageFromGallery2()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PickImageFromGallery() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

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
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row {
            Button(onClick = { /* Launch Gallery */ }) {
                Text(text = stringResource(id = R.string.galery))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { /* Launch Camera */ }) {
                Text(text = stringResource(id = R.string.camera))
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        encodeButton(imageUri?.toString())
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun PickImageFromGallery2() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(300.dp)
                .padding(10.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(size = 20.dp))
                .border(width = 2.dp, shape = RoundedCornerShape(size = 20.dp), color = Color(0xFF125E73))

        ) {
            imageUri?.let {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row {
            Button(
                onClick = { /* Launch Gallery */ },
                shape = RoundedCornerShape(size = 50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF125E73)),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.recurso_13),
                    contentDescription = "encode image",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { /* Launch Camera */ },
                shape = RoundedCornerShape(size = 50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF125E73)),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.recurso_2),
                    contentDescription = "encode image",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        encodeButton2(imageUri?.toString())
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun encodeButton(
    inputImage: String?
) {
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = message,
        onValueChange = { newMessage ->
            message = newMessage.take(70)
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

    Button(onClick = {
        isLoading = true
    }) {
        Text(text = stringResource(id = R.string.converter))
    }
    // ShareImageButton(imageEncodeUri)
}

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun encodeButton2(
    inputImage: String?
) {
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = message,
        onValueChange = { newMessage ->
            message = newMessage.take(70)
        },
        label = {
            Text(
                stringResource(id = R.string.limited_caracter),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = monserratRegular,
                    color = Color(0xFFFFFFFF)
                )
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

    Button(
        onClick = {
            isLoading = true
        },
        shape = RoundedCornerShape(size = 50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF93C464)),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.recurso_21),
            contentDescription = "encode image",
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = stringResource(id = R.string.converter),
            style = TextStyle(
                fontFamily = monserratMedium,
                color = Color(0xFF125E73),
                fontWeight = FontWeight.Bold
            )
        )
    }
    ShareImageButton()
}

@Composable
fun MyLoader() {
    LinearProgressIndicator(
        color =  Color(0xFF93C464),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun ShareImageButton() {

    var showDialog by remember { mutableStateOf(false) }
    var rememberCheckBoxState by remember { mutableStateOf(false) }



    Button(
        onClick = {
            if (rememberCheckBoxState) {
                // shareImage(context, uriToImage2)
            } else {
                showDialog = true
            }
        },
        shape = RoundedCornerShape(size = 50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF93C464)),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.recurso_11),
            contentDescription = "share button",
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(stringResource(id = R.string.share_image))
    }


}