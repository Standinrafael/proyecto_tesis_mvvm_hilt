package com.example.proyecto_tesis.ui.activities.baseScreens


import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.ui.routes.Routes
import com.example.proyecto_tesis.ui.theme.Purple40
import com.example.proyecto_tesis.ui.viewmodels.AuthViewModel
import com.example.proyecto_tesis.ui.viewmodels.FirestoreViewModel
import com.example.proyecto_tesis.utils.AuthRes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.proyecto_tesis.ui.activities.navScreens.DecodeScreen
import com.example.proyecto_tesis.ui.activities.navScreens.EncodeScreen
import com.example.proyecto_tesis.ui.activities.navScreens.InformationScreen
import com.example.proyecto_tesis.ui.routes.BottomNavScreen
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

    // Salir de la cuenta
    val onLogoutConfirmed: () -> Unit = {
        authViewModel.signOut()
        navigation.navigate(Routes.LogginScreen.route) {
            popUpTo(Routes.HomeScreen.route) {
                inclusive = true
            }
        }
    }

    // Eliminar la cuenta del usuario
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

                // Borrar historial y no dejar que se regrese a la pantalla anterior
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
                                placeholder = painterResource(id = R.drawable.ic_profilel),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(40.dp)
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.ic_profilel),
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
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = if (!user?.email.isNullOrEmpty()) "${user?.email}" else "Anónimo",
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(),
                actions = {
                    IconButton(
                        onClick = {
                            showDialogDelete = true
                        }
                    ) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Eliminar usuario")
                    }
                    IconButton(
                        onClick = {
                            showDialogExit = true
                        }
                    ) {
                        Icon(Icons.Outlined.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {

            // Diseño de los cuadros de diálogo
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

// Función para cerrar la sesión
@Composable
fun LogoutDialog(
    onConfirmLogout: () -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar sesión") },
        text = { Text("¿Estás seguro que deseas cerrar sesión?") },
        confirmButton = {
            Button(
                onClick = onConfirmLogout
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}

// Función para eliminar la cuenta
@Composable
fun DeleteDialog(
    onDeleteLogout: () -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar cuenta") },
        text = { Text("¿Estás seguro que deseas eliminar su cuenta?") },
        confirmButton = {
            Button(
                onClick = onDeleteLogout
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}

// Función para controlar la barra inferior
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
    NavigationBar {
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
        label = { Text(text = screen.title) },
        icon = { Icon(painter = painterResource(screen.icon), contentDescription = "Icons") },
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

// Navegación de la parte inferior
@Composable
fun BottomNavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = BottomNavScreen.Codification.route) {
        composable(route = BottomNavScreen.Codification.route) {
            EncodeScreen(navController)
        }
        composable(route = BottomNavScreen.Decodification.route) {
            DecodeScreen(navController)
        }
        composable(route = BottomNavScreen.Information.route) {
            InformationScreen()
        }
    }
}
