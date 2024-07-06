package com.example.proyecto_tesis.ui.activities.navScreens



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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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

@Composable
fun InformationScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Information(Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun Information(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 0.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        TitleText()
        Spacer(modifier = Modifier.height(50.dp))

        UceImage()
        Spacer(modifier = Modifier.height(50.dp))

        Developers(Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(50.dp))

        Tutor(Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(50.dp))

        Degree(Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(50.dp))

        Semester()
    }
}

@Composable
fun TitleText() {
    Text(
        text = stringResource(id = R.string.university_name),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun UceImage() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .padding(5.dp)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.ic_logo_uce),
            contentDescription = null,
        )
    }
}

@Composable
fun Developers(align: Modifier) {
    Text(
        modifier = align,
        text = stringResource(id = R.string.developers_label),
        style = MaterialTheme.typography.bodyLarge,
    )
    Spacer(modifier = Modifier.height(15.dp))
    Text(
        text = stringResource(id = R.string.developer_one),
        style = MaterialTheme.typography.bodyLarge
    )
    Text(
        text = stringResource(id = R.string.developer_two),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun Tutor(align: Modifier) {
    Text(
        modifier = align,
        text = stringResource(id = R.string.tutor_label),
        style = MaterialTheme.typography.bodyLarge
    )
    Spacer(modifier = Modifier.height(15.dp))
    Text(
        text = stringResource(id = R.string.tutor_name),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun Degree(align: Modifier) {
    Text(
        modifier = align,
        text = stringResource(id = R.string.degree_label),
        style = MaterialTheme.typography.bodyLarge
    )
    Spacer(modifier = Modifier.height(15.dp))
    Text(
        text = stringResource(id = R.string.degree_name),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun Semester() {
    Text(
        text = stringResource(id = R.string.semester),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun Preview() {
    InformationScreen()
}

