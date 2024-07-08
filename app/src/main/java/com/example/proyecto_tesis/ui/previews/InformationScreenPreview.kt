package com.example.proyecto_tesis.ui.previews

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.proyecto_tesis.R
import com.example.proyecto_tesis.utils.monserratLight
import com.example.proyecto_tesis.utils.monserratMedium
import com.example.proyecto_tesis.utils.monserratRegular
import com.example.proyecto_tesis.utils.monserratSemiBold

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
            .padding(horizontal = 40.dp, vertical = 0.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        UceImage()
        Spacer(modifier = Modifier.height(50.dp))

        Developers(
            Modifier
                .align(Alignment.Start)
                .fillMaxSize()
        )
        Spacer(modifier = Modifier.height(50.dp))

        Tutor(
            Modifier
                .align(Alignment.Start)
                .fillMaxSize()
        )
        Spacer(modifier = Modifier.height(50.dp))

        Degree(
            Modifier
                .align(Alignment.Start)
                .fillMaxSize()
        )
        Spacer(modifier = Modifier.height(50.dp))

        Semester()
    }
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
        style = TextStyle(
            color = Color(0xFF93C464),
            fontFamily = monserratMedium,
            letterSpacing = 0.125.em
        )
    )
    Spacer(modifier = Modifier.height(15.dp))
    Text(
        modifier = Modifier
            .padding(30.dp, 0.dp)
            .fillMaxSize(),
        text = stringResource(id = R.string.developer_one),
        style = TextStyle(
            fontSize = 14.sp,
            fontFamily = monserratRegular,
            color = Color(0xFFFFFFFF)
        )
    )
    Text(
        modifier = Modifier
            .padding(30.dp, 0.dp)
            .fillMaxSize(),
        text = stringResource(id = R.string.developer_two),
        style = TextStyle(
            fontSize = 14.sp,
            fontFamily = monserratRegular,
            color = Color(0xFFFFFFFF)
        )
    )
}

@Composable
fun Tutor(align: Modifier) {
    Text(
        modifier = align,
        text = stringResource(id = R.string.tutor_label),
        style = TextStyle(
            color = Color(0xFF93C464),
            fontFamily = monserratMedium,
            letterSpacing = 0.125.em
        )
    )
    Spacer(modifier = Modifier.height(15.dp))
    Text(
        modifier = Modifier
            .padding(30.dp, 0.dp)
            .fillMaxSize(),
        text = stringResource(id = R.string.tutor_name),
        style = TextStyle(
            fontSize = 14.sp,
            fontFamily = monserratRegular,
            color = Color(0xFFFFFFFF)
        )
    )
}

@Composable
fun Degree(align: Modifier) {
    Text(
        modifier = align,
        text = stringResource(id = R.string.degree_label),
        style = TextStyle(
            color = Color(0xFF93C464),
            fontFamily = monserratMedium,
            letterSpacing = 0.125.em
        )
    )
    Spacer(modifier = Modifier.height(15.dp))
    Text(
        modifier = Modifier
            .padding(30.dp, 0.dp)
            .fillMaxSize(),
        text = stringResource(id = R.string.degree_name),
        style = TextStyle(
            fontSize = 14.sp,
            fontFamily = monserratRegular,
            color = Color(0xFFFFFFFF)
        )
    )
}

@Composable
fun Semester() {
    Text(
        text = stringResource(id = R.string.semester),
        style = TextStyle(
            fontSize = 14.sp,
            fontFamily = monserratMedium,
            color = Color(0xFF93C464),
            letterSpacing = 0.125.em
        )
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun Preview() {
    InformationScreen()
}

