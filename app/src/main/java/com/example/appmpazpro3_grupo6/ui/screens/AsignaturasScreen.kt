package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmpazpro3_grupo6.ui.components.MPAZAsignaturaCard
import com.example.appmpazpro3_grupo6.ui.components.MPAZBackground
import com.example.appmpazpro3_grupo6.ui.components.MPAZScreenHeader
import com.example.appmpazpro3_grupo6.ui.components.MPAZSecondaryButton
import com.example.appmpazpro3_grupo6.ui.theme.*

private data class AsignaturaVisual(
    val titulo: String,
    val emoji: String,
    val fondo: Color,
    val nombreOriginal: String
)

@Composable
fun AsignaturasScreen(
    onVolverClick: () -> Unit = {},
    onAsignaturaClick: (String) -> Unit = {}
) {
    val asignaturas = listOf(
        AsignaturaVisual(
            titulo = "Lenguaje y Comunicación",
            emoji = "📖",
            fondo = MPAZRosaSuave,
            nombreOriginal = "📖 Lenguaje y Comunicación"
        ),
        AsignaturaVisual(
            titulo = "Matemática",
            emoji = "🔢",
            fondo = MPAZAzulSuave,
            nombreOriginal = "🔢 Matemática"
        ),
        AsignaturaVisual(
            titulo = "Ciencias Naturales",
            emoji = "🧪",
            fondo = MPAZVerdeSuave,
            nombreOriginal = "🧪 Ciencias Naturales"
        ),
        AsignaturaVisual(
            titulo = "Historia, Geografía y Ciencias Sociales",
            emoji = "🌎",
            fondo = MPAZAmarilloSuave,
            nombreOriginal = "🌎 Historia, Geografía y Ciencias Sociales"
        )
    )

    MPAZBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            MPAZScreenHeader(
                titulo = "Mis asignaturas",
                subtitulo = "¡Elige qué quieres aprender hoy!",
                onVolverClick = onVolverClick
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                asignaturas.chunked(2).forEach { fila ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {

                        fila.forEach { asignatura ->

                            MPAZAsignaturaCard(
                                titulo = asignatura.titulo,
                                emoji = asignatura.emoji,
                                fondo = asignatura.fondo,
                                onClick = {
                                    onAsignaturaClick(
                                        asignatura.nombreOriginal
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(190.dp)
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "¡Cada asignatura es una nueva aventura!",
                fontSize = 15.sp,
                color = MPAZTextoSecundario
            )

            MPAZSecondaryButton(
                texto = "Volver al perfil",
                onClick = onVolverClick,
                modifier = Modifier.widthIn(max = 480.dp)
            )
        }
    }
}

@Preview(
    name = "MPAZ - Asignaturas iguales",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun AsignaturasScreenPreview() {
    AppMPAZPRO3_Grupo6Theme {
        AsignaturasScreen()
    }
}