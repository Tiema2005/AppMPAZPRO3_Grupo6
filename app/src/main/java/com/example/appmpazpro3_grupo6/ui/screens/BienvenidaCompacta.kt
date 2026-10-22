package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmpazpro3_grupo6.ui.components.MPAZBackground
import com.example.appmpazpro3_grupo6.ui.components.MPAZButton
import com.example.appmpazpro3_grupo6.ui.components.MPAZLogo
import com.example.appmpazpro3_grupo6.ui.components.MascotaMPAZ
import com.example.appmpazpro3_grupo6.ui.theme.AppMPAZPRO3_Grupo6Theme
import com.example.appmpazpro3_grupo6.ui.theme.MPAZTextoSecundario

@Composable
fun BienvenidaCompacta(
    onComenzarClick: () -> Unit = {}
) {
    MPAZBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 24.dp,
                    vertical = 28.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            MascotaMPAZ(
                modifier = Modifier.size(235.dp)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            MPAZLogo(
                mostrarLema = true
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Escuela Escritora Marcela Paz\nTercero Básico",
                fontSize = 15.sp,
                color = MPAZTextoSecundario,
                textAlign = TextAlign.Center,
                lineHeight = 23.sp
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            MPAZButton(
                texto = "Comenzar →",
                onClick = onComenzarClick
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            IndicadoresBienvenida()
        }
    }
}

@Preview(
    name = "MPAZ - Bienvenida compacta Pixel 6",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PreviewBienvenidaCompacta() {
    AppMPAZPRO3_Grupo6Theme {
        BienvenidaCompacta()
    }
}