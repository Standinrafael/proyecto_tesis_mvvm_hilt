package com.example.proyecto_tesis.ui.routes

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.example.proyecto_tesis.ui.viewmodels.AuthViewModel

@Composable
fun AppNavigation(context: Context,navController: NavHostController = rememberNavController()) {


    val authViewModel: AuthViewModel = hiltViewModel()
    val currentUser by authViewModel.currentUser.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (currentUser == null) Routes.SplashScreen.route else Routes.HomeScreen.route
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