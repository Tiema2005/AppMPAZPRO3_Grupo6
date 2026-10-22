package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.appmpazpro3_grupo6.ui.components.MPAZBackground
import com.example.appmpazpro3_grupo6.ui.components.MPAZButton
import com.example.appmpazpro3_grupo6.ui.components.MPAZLogo
import com.example.appmpazpro3_grupo6.ui.components.MascotaMPAZ
import com.example.appmpazpro3_grupo6.ui.theme.AppMPAZPRO3_Grupo6Theme
import com.example.appmpazpro3_grupo6.ui.theme.MPAZTexto
import com.example.appmpazpro3_grupo6.ui.theme.MPAZTextoSecundario

@Composable
fun BienvenidaExpandida(
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = alturaDisponible)
                        .padding(40.dp),
                    horizontalArrangement = Arrangement.spacedBy(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        MascotaMPAZ(
                            modifier = Modifier.size(285.dp)
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        MPAZLogo(
                            mostrarLema = false
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {

                        Text(
                            text = "¡Aprender es una aventura!",
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Bold,
                            color = MPAZTexto,
                            textAlign = TextAlign.Center,
                            lineHeight = 42.sp
                        )

                        Text(
                            text = "Escuela Escritora Marcela Paz",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Medium,
                            color = MPAZTexto,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Tu plataforma de aprendizaje para Tercero Básico",
                            fontSize = 19.sp,
                            color = MPAZTextoSecundario,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        MPAZButton(
                            texto = "Comenzar →",
                            onClick = onComenzarClick,
                            modifier = Modifier.widthIn(max = 440.dp)
                        )

                        IndicadoresBienvenida()
                    }
                }
            }
        }
    }
}

@Preview(
    name = "MPAZ - Bienvenida expandida Tablet",
    showBackground = true,
    widthDp = 1000,
    heightDp = 650
)
@Composable
fun PreviewBienvenidaExpandida() {
    AppMPAZPRO3_Grupo6Theme {
        BienvenidaExpandida()
    }
}