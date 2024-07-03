package com.example.proyecto_tesis.ui.routes

sealed class Routes(val route: String) {
    object SplashScreen : Routes("splashScreen")
    object ExceptionScreen : Routes("exceptionScreen")
    object LogginScreen : Routes("logginScreen")
    object HomeScreen : Routes("homeScreen")
    object SignUpScreen : Routes("signUpScreen")
    object ForgotPasswordScreen : Routes("forgotPasswordScreen")
}