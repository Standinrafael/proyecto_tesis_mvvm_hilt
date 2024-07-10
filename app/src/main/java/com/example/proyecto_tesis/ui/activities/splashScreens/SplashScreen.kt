package com.example.proyecto_tesis.ui.activities.splashScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.ui.routes.Routes
import kotlinx.coroutines.delay
import androidx.compose.ui.unit.em
import com.example.proyecto_tesis.ui.theme.color_azul
import com.example.proyecto_tesis.ui.theme.color_verde
import com.example.proyecto_tesis.utils.monserratSemiBold


@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(key1 = true) {
        delay(5000)
        navController.popBackStack()
        navController.navigate(Routes.ExceptionScreen.route)
    }
    Splash()
}

@Composable
fun Splash() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.archivo), contentDescription = "Logo",
            Modifier.size(80.dp, 100.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                stringResource(id = R.string.hide),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = monserratSemiBold,
                color = color_azul,
                letterSpacing = 0.25.em
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                stringResource(id = R.string.snap),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = monserratSemiBold,
                color = color_verde,
                letterSpacing = 0.25.em
            )
        }


    }
}

