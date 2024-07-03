package com.example.proyecto_tesis.ui.routes

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyecto_tesis.ui.activities.authScreens.ForgotPasswordScreen
import com.example.proyecto_tesis.ui.activities.authScreens.LogginScreen
import com.example.proyecto_tesis.ui.activities.authScreens.SignUpScreen
import com.example.proyecto_tesis.ui.activities.baseScreens.HomeScreen
import com.example.proyecto_tesis.ui.activities.splashScreens.ExceptionScreen
import com.example.proyecto_tesis.ui.activities.splashScreens.SplashScreen
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {

    NavHost(
        navController = navController,
        startDestination = Routes.SplashScreen.route
    ) {
        composable(Routes.SplashScreen.route) {
            SplashScreen(navController)
        }

        composable(Routes.ExceptionScreen.route) {
            ExceptionScreen(navController)
        }

        composable(Routes.LogginScreen.route) {

            LogginScreen(navController)
        }

        composable(Routes.HomeScreen.route) {
           HomeScreen(navController)
        }

        composable(Routes.SignUpScreen.route) {
            SignUpScreen(navController)
        }

        composable(Routes.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(navigation = navController)
        }
    }
}