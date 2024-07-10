package com.example.proyecto_tesis.ui.activities.authScreens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto_tesis.ui.viewmodels.AuthViewModel
import com.example.proyecto_tesis.utils.AuthRes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.launch
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.ui.theme.color_azul
import com.example.proyecto_tesis.ui.theme.color_verde
import com.example.proyecto_tesis.utils.monserratLight
import com.example.proyecto_tesis.utils.monserratMedium
import com.example.proyecto_tesis.utils.monserratSemiBold

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUpScreen(
    navigation: NavController
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val authViewModel: AuthViewModel = hiltViewModel()

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
                stringResource(id = R.string.hide),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = monserratSemiBold,
                color = color_azul,
                letterSpacing = 0.25.em
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                stringResource(id = R.string.snap),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = monserratSemiBold,
                color = color_verde,
                letterSpacing = 0.25.em
            )
        }
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = stringResource(id = R.string.create_account),
            style = TextStyle(fontSize = 30.sp,  color = color_verde, fontFamily = monserratSemiBold)
        )
        Spacer(modifier = Modifier.height(50.dp))
        TextField(
            label = {
                Text(text = stringResource(id = R.string.email),
                    fontFamily = monserratLight,
                    color = Color.Black) },
            value = email,
            modifier = Modifier.height(60.dp),
            shape =RoundedCornerShape(15.dp) ,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it }
        )
        Spacer(modifier = Modifier.height(40.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(text = stringResource(id = R.string.password),
                    fontFamily = monserratLight,
                    color = Color.Black) },

            modifier = Modifier.height(60.dp),
            shape =RoundedCornerShape(15.dp) ,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisibility) stringResource(id = R.string.hide_password) else stringResource(
                            id = R.string.show_password
                        )
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.password_minimum),
            style = TextStyle(fontSize = 10.sp, fontFamily = monserratLight,
                color = color_verde)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    if (email.contains("@")) {
                        scope.launch {
                            val signUpResult = authViewModel.signUp(email, password)
                            signUpResult.observe(context as LifecycleOwner) { result ->
                                when (result) {
                                    is AuthRes.Success -> {
                                        Toast.makeText(
                                            context,
                                            "Registro exitoso",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navigation.popBackStack()
                                    }

                                    is AuthRes.Error -> {
                                        Toast.makeText(
                                            context,
                                            "Error de registro: ${result.errorMessage}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Formato de correo electrónico inválido",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = color_verde),
                modifier = Modifier
                    .width(230.dp)
                    .height(50.dp)
            ) {
                Text(text = stringResource(id = R.string.sign_up), fontFamily = monserratMedium, color = color_azul,fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        ClickableText(
            text = AnnotatedString(stringResource(id = R.string.already_have_account)),
            onClick = {
                navigation.popBackStack()
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = monserratLight,
                color = color_verde
            )
        )
    }
}
