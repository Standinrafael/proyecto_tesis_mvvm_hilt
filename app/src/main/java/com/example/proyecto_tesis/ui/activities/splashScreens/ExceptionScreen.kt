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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.ui.previews.DecodeScreenPreview
import com.example.proyecto_tesis.ui.routes.Routes
import com.example.proyecto_tesis.ui.theme.Proyecto_tesisTheme
import com.example.proyecto_tesis.utils.monserratLight
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
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "INICIANDO",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = monserratMedium,
                color = Color(0xFF93C464),
                letterSpacing = 0.25.em
            )
        )
        Text(
            text = "SESION",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = monserratMedium,
                color = Color(0xFF93C464),
                letterSpacing = 0.25.em
            )
        )
        Spacer(modifier = Modifier.height(70.dp))
        Text(
            text = "Las imagenes generadas por HIDESNAP   enviadas a travez de aplicaciones de META " +
                    "no podrán ser decodificadas, debido a politicas privadas. ",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = monserratMedium,
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.recurso_8),
                contentDescription = "Logo",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(id = R.drawable.recurso_9),
                contentDescription = "Logo",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(id = R.drawable.recurso_4),
                contentDescription = "Logo",
                modifier = Modifier.size(60.dp)
            )
        }
    }
}