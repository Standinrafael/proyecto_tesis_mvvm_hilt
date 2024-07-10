package com.example.proyecto_tesis.ui.routes

import com.example.proyecto_tesis.R

sealed class BottomNavScreen(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Codification : BottomNavScreen(
        route = "codification",
        title = "Codificación",
        icon = R.drawable.ic_lock
    )

    object Decodification : BottomNavScreen(
        route = "decodification",
        title = "Decodificación",
        icon = R.drawable.ic_lock_open,

    )

    object Information : BottomNavScreen(
        route = "information",
        title = "Información",
        icon = R.drawable.ic_info
    )
}