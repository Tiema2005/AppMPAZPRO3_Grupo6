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
fun BienvenidaMediana(
    onComenzarClick: () -> Unit = {}
) {
    MPAZBackground {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val alturaDisponible = maxHeight

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = alturaDisponible)
                        .padding(
                            horizontal = 36.dp,
                            vertical = 28.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    MascotaMPAZ(
                        modifier = Modifier.size(280.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    MPAZLogo(
                        mostrarLema = true
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Text(
                        text = "Escuela Escritora Marcela Paz\nTercero Básico",
                        fontSize = 18.sp,
                        color = MPAZTextoSecundario,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )

                    Spacer(
                        modifier = Modifier.height(36.dp)
                    )

                    MPAZButton(
                        texto = "Comenzar",
                        onClick = onComenzarClick,
                        modifier = Modifier.widthIn(max = 440.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    IndicadoresBienvenida()
                }
            }
        }
    }
}

@Preview(
    name = "MPAZ - Bienvenida mediana",
    showBackground = true,
    widthDp = 700,
    heightDp = 900
)
@Composable
fun PreviewBienvenidaMediana() {
    AppMPAZPRO3_Grupo6Theme {
        BienvenidaMediana()
    }
}