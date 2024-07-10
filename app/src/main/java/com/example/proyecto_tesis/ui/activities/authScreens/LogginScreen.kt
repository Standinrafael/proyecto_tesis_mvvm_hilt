package com.example.proyecto_tesis.ui.activities.authScreens

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.ui.routes.Routes
import com.example.proyecto_tesis.ui.viewmodels.AuthViewModel
import com.example.proyecto_tesis.ui.viewmodels.FirestoreViewModel
import com.example.proyecto_tesis.utils.AuthRes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.example.proyecto_tesis.ui.theme.color_azul
import com.example.proyecto_tesis.ui.theme.color_verde
import com.example.proyecto_tesis.utils.monserratLight
import com.example.proyecto_tesis.utils.monserratMedium
import com.example.proyecto_tesis.utils.monserratSemiBold


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LogginScreen(navigation: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val authViewModel: AuthViewModel = hiltViewModel()
    val firestoreViewModel: FirestoreViewModel = hiltViewModel()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (val account =
            authViewModel.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))) {
            is AuthRes.Success -> {
                val credential = GoogleAuthProvider.getCredential(account.data?.idToken, null)
                val googleAuthResult = authViewModel.signInWithGoogleCredential(credential)
                googleAuthResult.observe(context as LifecycleOwner) { fireUser ->
                    when (fireUser) {
                        is AuthRes.Success -> {
                            Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show()
                            navigation.navigate(Routes.HomeScreen.route) {
                                popUpTo(Routes.LogginScreen.route) {
                                    inclusive = true
                                }
                            }
                        }

                        is AuthRes.Error -> {
                            Toast.makeText(
                                context,
                                "Error al abrir sesión, intente de nuevo",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            Toast.makeText(context, "Error desconocido", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            is AuthRes.Error -> {
                Toast.makeText(
                    context,
                    "Error al abrir sesión, intente de nuevo",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                Toast.makeText(context, "Error desconocido", Toast.LENGTH_SHORT).show()
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(
            text = AnnotatedString(stringResource(id = R.string.question_create_account)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = {
                navigation.navigate(Routes.SignUpScreen.route)
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = monserratLight,
                color = color_verde
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
            modifier = Modifier.size(60.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
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
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = stringResource(id = R.string.login),
            style = TextStyle(
                fontSize = 30.sp,
                color = color_verde,
                fontFamily = monserratSemiBold,
                letterSpacing = 0.25.em
            )
        )
        Spacer(modifier = Modifier.height(30.dp))
        TextField(
            label = {
                Text(
                    text = stringResource(id = R.string.email),
                    fontFamily = monserratLight,
                    color = Color.Black
                )
            },
            value = email,
            modifier = Modifier.height(60.dp),
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it })
        Spacer(modifier = Modifier.height(40.dp))
        TextField(
            value = password,
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.height(60.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedIndicatorColor = Color.Transparent
            ),
            onValueChange = { password = it },
            label = {
                Text(
                    text = stringResource(id = R.string.password),
                    fontFamily = monserratLight,
                    color = Color.Black
                )
            },
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
                    if (email.contains("@")) {
                        val signInResult = authViewModel.signInWithEmailandPassword(email, password)
                        signInResult.observe(context as LifecycleOwner) { result ->
                            when (result) {
                                is AuthRes.Success -> {
                                    navigation.navigate(Routes.HomeScreen.route) {
                                        popUpTo(Routes.LogginScreen.route) {
                                            inclusive = true
                                        }
                                    }
                                }

                                is AuthRes.Error -> {
                                    Toast.makeText(
                                        context,
                                        "Error al iniciar sesión: usuario o contraseña incorrectos",
                                        Toast.LENGTH_SHORT
                                    ).show()
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
                Text(
                    text = stringResource(id = R.string.sign_in),
                    fontFamily = monserratMedium,
                    color = color_azul,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString(stringResource(id = R.string.forgot_password)),
            onClick = {
                navigation.navigate(Routes.ForgotPasswordScreen.route)
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = monserratLight,
                color = color_verde
            )
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "_________________________________________",
            style = TextStyle(color = color_verde)
        )
        Spacer(modifier = Modifier.height(25.dp))
        SocialMediaButton(
            onClick = {
                authViewModel.signInWithGoogle(googleSignInLauncher)
            },
            text = stringResource(id = R.string.sign_in_google),
            icon = R.drawable.ic_google,
            color = Color(0xFFFFFCFC)
        )
    }
}

@Composable
fun SocialMediaButton(onClick: () -> Unit, text: String, icon: Int, color: Color) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .padding(start = 40.dp, end = 40.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, color = Color.Gray),
        color = color
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                modifier = Modifier.size(24.dp),
                contentDescription = text,
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, color = Color.Black)
        }
    }
}