package com.example.proyecto_tesis.ui.activities.authScreens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LogginScreen (navigation: NavController) {
    Text(
        "Loggin",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
}