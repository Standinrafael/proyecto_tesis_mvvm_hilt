package com.example.proyecto_tesis.ui.previews

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.ui.activities.authScreens.SocialMediaButton
import com.example.proyecto_tesis.ui.theme.Proyecto_tesisTheme
import com.example.proyecto_tesis.utils.monserratLight
import com.example.proyecto_tesis.utils.monserratMedium
import com.example.proyecto_tesis.utils.monserratSemiBold

@Preview(showSystemUi = true)
@Composable
fun PreviewLogginScreen() {
    Proyecto_tesisTheme {
        LogginScreenPreview(navigation = rememberNavController())
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogginScreenPreview(
    navigation: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(
            text = AnnotatedString(stringResource(id = R.string.question_create_account)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = {
                // No hacer nada en la vista previa
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                color = Color(0xFF93C464)
            )
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.archivo),
            contentDescription = "Logo",
            modifier = Modifier.size(70.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically){
            Text(
                "H I D E",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = monserratSemiBold,
                color = Color(0xFF125E73)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                "S N A P",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = monserratSemiBold,
                color = Color(0xFF93C464)
            )
        }
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = stringResource(id = R.string.login),
            style = TextStyle(fontSize = 30.sp,
                color = Color(0xFF93C464),
                fontFamily = monserratSemiBold,
            )
        )

        Spacer(modifier = Modifier.height(30.dp))
        TextField(
            label = { Text(text = stringResource(id = R.string.email), fontFamily = monserratLight, color = Color.Gray) },
            value = email,
            modifier = Modifier.height(30.dp),
            shape =RoundedCornerShape(15.dp) ,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent

                ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it })
        Spacer(modifier = Modifier.height(30.dp))
        TextField(
            value = password,
            shape =RoundedCornerShape(15.dp) ,
            modifier = Modifier.height(30.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            onValueChange = { password = it },
            label = { Text(text = stringResource(id = R.string.password), fontFamily = monserratLight, color = Color.Gray) },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisibility) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(50.dp))

        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    // No hacer nada en la vista previa
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF93C464)),
                modifier = Modifier
                    .width(230.dp)
                    .height(50.dp)
            ) {
                Text(text = stringResource(id = R.string.sign_in), fontFamily = monserratMedium, color = Color(0xFF125E73),fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString(stringResource(id = R.string.forgot_password)),
            onClick = {
                // No hacer nada en la vista previa
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = monserratLight,
                color = Color(0xFF93C464)
            )
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(text = "_________________________________________", style = TextStyle(color = Color(0xFF93C464)))
        Spacer(modifier = Modifier.height(25.dp))
        SocialMediaButton(
            onClick = {
                // No hacer nada en la vista previa
            },
            text = stringResource(id = R.string.sign_in_google),
            icon = R.drawable.ic_google,
            color = Color(0xFFFFFCFC)
        )
    }
}