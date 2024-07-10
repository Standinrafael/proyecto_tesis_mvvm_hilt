package com.example.proyecto_tesis.ui.activities.splashScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.ui.routes.Routes
import com.example.proyecto_tesis.ui.theme.color_azul
import com.example.proyecto_tesis.ui.theme.color_blanco
import com.example.proyecto_tesis.ui.theme.color_verde
import com.example.proyecto_tesis.utils.monserratMedium
import com.example.proyecto_tesis.utils.monserratSemiBold
import kotlinx.coroutines.delay

@Composable
fun ExceptionScreen(navController: NavController) {
    LaunchedEffect(key1 = true) {
        delay(5000)
        navController.popBackStack()
        navController.navigate(Routes.LogginScreen.route)
    }
    SplashException()
}

@Composable
fun SplashException() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.archivo),
            contentDescription = "Logo",
            modifier = Modifier.size(70.dp)
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
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = stringResource(id = R.string.initiating),
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = monserratMedium,
                color = color_verde,
                letterSpacing = 0.25.em
            )
        )
        Text(
            text = stringResource(id = R.string.sesion),
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = monserratMedium,
                color = color_verde,
                letterSpacing = 0.25.em
            )
        )
        Spacer(modifier = Modifier.height(70.dp))
        Text(
            text = stringResource(id = R.string.meta_message),
            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = monserratMedium,
                color = color_blanco,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.messenger_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(id = R.drawable.whatsapp_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(id = R.drawable.instagram_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(60.dp)
            )
        }
    }
}