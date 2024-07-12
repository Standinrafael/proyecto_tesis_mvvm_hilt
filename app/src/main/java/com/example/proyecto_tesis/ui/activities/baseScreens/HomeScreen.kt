package com.example.proyecto_tesis.ui.activities.baseScreens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.ui.routes.Routes
import com.example.proyecto_tesis.ui.viewmodels.AuthViewModel
import com.example.proyecto_tesis.ui.viewmodels.FirestoreViewModel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.proyecto_tesis.ui.routes.BottomNavGraph
import com.example.proyecto_tesis.ui.routes.BottomNavScreen
import com.example.proyecto_tesis.ui.theme.BackgroundColor
import com.example.proyecto_tesis.ui.theme.color_azul
import com.example.proyecto_tesis.ui.theme.color_barra
import com.example.proyecto_tesis.ui.theme.color_blanco
import com.example.proyecto_tesis.ui.theme.color_verde
import com.example.proyecto_tesis.utils.monserratMedium
import com.example.proyecto_tesis.utils.monserratRegular
import com.example.proyecto_tesis.utils.monserratSemiBold
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navigation: NavController) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val authViewModel: AuthViewModel = hiltViewModel()
    val firestoreViewModel: FirestoreViewModel = hiltViewModel()
    val user by authViewModel.currentUser.collectAsState(initial = null)
    var showDialogExit by remember { mutableStateOf(false) }
    var showDialogDelete by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val onLogoutConfirmed: () -> Unit = {
        authViewModel.signOut()
        navigation.navigate(Routes.LogginScreen.route) {
            popUpTo(Routes.HomeScreen.route) {
                inclusive = true
            }
        }
    }

    val onDeleteConfirmed: () -> Unit = {
        val uid = authViewModel.getCurrentUid()
        if (uid != null) {
            scope.launch {
                val deleteUserSuccess = authViewModel.deleteCurrentUser()
                val deleteUserFirestore = firestoreViewModel.deleteUserByAuthUID(uid)
                if (deleteUserSuccess && deleteUserFirestore) {
                    Toast.makeText(
                        context,
                        "Cuenta eliminada satisfactoriamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                authViewModel.signOut()
                navigation.navigate(Routes.LogginScreen.route) {
                    popUpTo(Routes.HomeScreen.route) {
                        inclusive = true
                    }
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (user?.photoUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user?.photoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Imagen",
                                placeholder = painterResource(id = R.drawable.recurso_15),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(40.dp)
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.icono_app_2),
                                contentDescription = "Foto de perfil por defecto",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (!user?.displayName.isNullOrEmpty()) "Hola ${user?.displayName}" else "Bienvenido",
                                fontSize = 20.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = color_blanco
                            )
                            Text(
                                text = if (!user?.email.isNullOrEmpty()) "${user?.email}" else "Anónimo",
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = color_verde
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = color_barra
                ),
                actions = {
                    IconButton(
                        onClick = {
                            showDialogDelete = true
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Delete,
                            tint = color_verde,
                            contentDescription = stringResource(id = R.string.delete_user)
                        )
                    }
                    IconButton(
                        onClick = {
                            showDialogExit = true
                        }
                    ) {
                        Icon(
                            Icons.Outlined.ExitToApp,
                            tint = color_verde,
                            contentDescription = stringResource(id = R.string.close_sesion)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(navController = navController)
        },

        ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            if (showDialogExit) {
                LogoutDialog(onConfirmLogout = {
                    onLogoutConfirmed()
                    showDialogExit = false
                }, onDismiss = { showDialogExit = false })
            }
            if (showDialogDelete) {
                DeleteDialog(onDeleteLogout = {
                    onDeleteConfirmed()
                    showDialogDelete = false
                }, onDismiss = { showDialogDelete = false })
            }
            BottomNavGraph(navController = navController)
        }
    }
}


@Composable
fun LogoutDialog(
    onConfirmLogout: () -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BackgroundColor,
        title = {
            Text(
                stringResource(id = R.string.close_sesion),
                style = TextStyle(
                    fontSize = 17.sp,
                    fontFamily = monserratSemiBold,
                    color = color_verde
                )
            )
        },
        text = {
            Text(
                stringResource(id = R.string.question_close_sesion),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = monserratRegular,
                    color = color_blanco
                )
            )
        },
        confirmButton = {
            Button(
                shape = RoundedCornerShape(size = 50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = color_verde),
                onClick = onConfirmLogout
            ) {
                Text(
                    stringResource(id = R.string.yes),
                    style = TextStyle(
                        fontFamily = monserratMedium,
                        color = color_azul
                    )
                )
            }
        },
        dismissButton = {
            Button(
                shape = RoundedCornerShape(size = 50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = color_verde),
                onClick = onDismiss
            ) {
                Text(
                    stringResource(id = R.string.no),
                    style = TextStyle(
                        fontFamily = monserratMedium,
                        color = color_azul
                    )
                )
            }
        }
    )
}

@Composable
fun DeleteDialog(
    onDeleteLogout: () -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BackgroundColor,
        title = {
            Text(
                stringResource(id = R.string.delete_account),
                style = TextStyle(
                    fontSize = 17.sp,
                    fontFamily = monserratSemiBold,
                    color = color_verde
                )
            )
        },
        text = {
            Text(
                stringResource(id = R.string.question_delete_user),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = monserratRegular,
                    color = color_blanco
                )
            )
        },
        confirmButton = {
            Button(
                shape = RoundedCornerShape(size = 50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = color_verde),
                onClick = onDeleteLogout
            ) {
                Text(
                    stringResource(id = R.string.yes),
                    style = TextStyle(
                        fontFamily = monserratMedium,
                        color = color_azul
                    )
                )
            }
        },
        dismissButton = {
            Button(
                shape = RoundedCornerShape(size = 50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = color_verde),
                onClick = onDismiss
            ) {
                Text(
                    stringResource(id = R.string.no),
                    style = TextStyle(
                        fontFamily = monserratMedium,
                        color = color_azul
                    )
                )
            }
        }
    )
}


@Composable
fun BottomBar(
    navController: NavHostController
) {
    val screens = listOf(
        BottomNavScreen.Codification,
        BottomNavScreen.Decodification,
        BottomNavScreen.Information
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar(containerColor = color_barra) {
        screens.forEach { screen ->
            if (currentDestination != null) {
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavScreen,
    currentDestination: NavDestination,
    navController: NavHostController
) {
    NavigationBarItem(
        label = { Text(text = screen.title, color = color_verde) },
        icon = {
            Icon(
                painter = painterResource(screen.icon),
                contentDescription = "Icons",
                tint = color_verde
            )
        },
        selected = currentDestination.hierarchy.any {
            it.route == screen.route
        },
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.id)
                launchSingleTop = true
            }
        }
    )
}