package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmpazpro3_grupo6.ui.components.MPAZBackground
import com.example.appmpazpro3_grupo6.ui.components.MPAZScreenHeader
import com.example.appmpazpro3_grupo6.ui.components.MPAZButton
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
            titulo = "Historia y Geografía",
            emoji = "🌎",
            fondo = MPAZAmarilloSuave,
            nombreOriginal =
                "🌎 Historia, Geografía y Ciencias Sociales"
        )
    )

    MPAZBackground {

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {

            val alturaPantalla = maxHeight

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = alturaPantalla)
                        .padding(
                            horizontal = 20.dp,
                            vertical = 24.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Column(
                        modifier = Modifier
                            .widthIn(max = 600.dp)
                            .fillMaxWidth(),
                        horizontalAlignment =
                            Alignment.CenterHorizontally,
                        verticalArrangement =
                            Arrangement.spacedBy(20.dp)
                    ) {

                        MPAZScreenHeader(
                            titulo = "Mis asignaturas",
                            subtitulo =
                                "¡Elige qué quieres aprender hoy!",
                            onVolverClick = onVolverClick
                        )

                        Spacer(Modifier.height(4.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement =
                                Arrangement.spacedBy(12.dp)
                        ) {

                            asignaturas.chunked(2).forEach { fila ->

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement =
                                        Arrangement.spacedBy(12.dp)
                                ) {

                                    fila.forEach { asignatura ->

                                        TarjetaAsignaturaCentrada(
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
                                                .height(180.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "¡Cada asignatura es una nueva aventura!",
                            fontSize = 15.sp,
                            color = MPAZTextoSecundario,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        MPAZButton(
                            texto = "Volver al perfil",
                            onClick = onVolverClick,
                            modifier = Modifier
                                .widthIn(max = 240.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaAsignaturaCentrada(
    titulo: String,
    emoji: String,
    fondo: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = fondo,
        shadowElevation = 4.dp
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment =
                    Alignment.CenterHorizontally,
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = emoji,
                    fontSize = 38.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = titulo,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MPAZTexto,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(
    name = "MPAZ - Asignaturas centradas",
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