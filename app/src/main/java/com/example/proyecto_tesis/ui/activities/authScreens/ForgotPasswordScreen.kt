package com.example.proyecto_tesis.ui.activities.authScreens


import android.widget.Toast
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

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.proyecto_tesis.ui.routes.Routes
import com.example.proyecto_tesis.ui.theme.Purple40
import com.example.proyecto_tesis.ui.viewmodels.AuthViewModel

import com.example.proyecto_tesis.utils.AuthRes

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.ui.theme.Proyecto_tesisTheme
import com.example.proyecto_tesis.utils.monserratLight
import com.example.proyecto_tesis.utils.monserratMedium
import com.example.proyecto_tesis.utils.monserratSemiBold
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navigation: NavController
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
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
            text = stringResource(id = R.string.forgot_password),
            style = TextStyle(fontSize = 25.sp, color = Color(0xFF93C464))
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = stringResource(id = R.string.forget_passwor_message),
            fontSize = 12.sp,
            fontFamily = monserratMedium,
            color = Color.White,
            textAlign = TextAlign.Center

        )
        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            label = { Text(text = stringResource(id = R.string.email_hint), fontFamily = monserratLight, color = Color.Gray) },
            value = email,
            modifier = Modifier.height(30.dp),
            shape =RoundedCornerShape(15.dp) ,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent

            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it }
        )
        Spacer(modifier = Modifier.height(50.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    if (email.contains("@")) {
                        scope.launch {
                            val resetResult = authViewModel.resetPassword(email)
                            resetResult.observe(context as LifecycleOwner) { res ->
                                when (res) {
                                    is AuthRes.Success -> {
                                        Toast.makeText(
                                            context,
                                            "Correo enviado",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        navigation.navigate(Routes.LogginScreen.route)
                                    }

                                    is AuthRes.Error -> {
                                        Toast.makeText(
                                            context,
                                            "Error al enviar el correo: ${res.errorMessage}",
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF93C464)),
                modifier = Modifier
                    .width(230.dp)
                    .height(50.dp)
            ) {
                Text(stringResource(id = R.string.recover_password),fontFamily = monserratMedium, color = Color(0xFF125E73),fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        ClickableText(
            text = AnnotatedString(stringResource(id = R.string.go_to_home)),
            onClick = {
                navigation.popBackStack()
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = monserratLight,
                color = Color(0xFF93C464)
            )
        )
    }
}
