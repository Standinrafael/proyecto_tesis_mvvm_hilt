package com.example.proyecto_tesis.ui.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyecto_tesis.ui.activities.navScreens.DecodeScreen
import com.example.proyecto_tesis.ui.activities.navScreens.EncodeScreen
import com.example.proyecto_tesis.ui.activities.navScreens.InformationScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavScreen.Codification.route) {
        composable(route = BottomNavScreen.Codification.route) {
            EncodeScreen()
        }
        composable(route = BottomNavScreen.Decodification.route) {
            DecodeScreen()
        }
        composable(route = BottomNavScreen.Information.route) {
            InformationScreen()
        }
    }
}