package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmpazpro3_grupo6.ui.components.MPAZBackground
import com.example.appmpazpro3_grupo6.ui.components.MPAZButton
import com.example.appmpazpro3_grupo6.ui.components.MPAZCard
import com.example.appmpazpro3_grupo6.ui.components.MPAZScreenHeader
import com.example.appmpazpro3_grupo6.ui.theme.*

private data class ContenidoAsignatura(
    val titulo: String,
    val emoji: String,
    val descripcion: String,
    val pregunta: String,
    val respuestas: List<String>,
    val correcta: String,
    val fondo: Color
)

@Composable
fun AsignaturaDetalleScreen(
    asignaturaId: String,
    onVolverClick: () -> Unit = {}
) {

    val asignatura = when (asignaturaId) {

        "lenguaje" -> ContenidoAsignatura(
            titulo = "Lenguaje y Comunicación",
            emoji = "📖",
            descripcion = "Aprenderemos sobre lectura, " +
                    "escritura y comprensión de textos.",
            pregunta = "¿Cuál de estas palabras es un sustantivo?",
            respuestas = listOf(
                "Correr",
                "Mesa",
                "Rápido"
            ),
            correcta = "Mesa",
            fondo = MPAZRosaSuave
        )

        "matematica" -> ContenidoAsignatura(
            titulo = "Matemática",
            emoji = "🔢",
            descripcion = "Aprenderemos a trabajar con " +
                    "números y resolver problemas.",
            pregunta = "¿Cuánto es 7 + 5?",
            respuestas = listOf(
                "10",
                "12",
                "14"
            ),
            correcta = "12",
            fondo = MPAZAzulSuave
        )

        "ciencias" -> ContenidoAsignatura(
            titulo = "Ciencias Naturales",
            emoji = "🧪",
            descripcion = "Descubriremos los seres vivos " +
                    "y el mundo que nos rodea.",
            pregunta = "¿Qué necesitan las plantas para crecer?",
            respuestas = listOf(
                "Luz y agua",
                "Solo piedras",
                "Solo plástico"
            ),
            correcta = "Luz y agua",
            fondo = MPAZVerdeSuave
        )

        "historia" -> ContenidoAsignatura(
            titulo = "Historia y Geografía",
            emoji = "🌎",
            descripcion = "Conoceremos lugares, mapas " +
                    "y nuestro entorno.",
            pregunta = "¿Qué representa un mapa?",
            respuestas = listOf(
                "Lugares y territorios",
                "Recetas de cocina",
                "Canciones"
            ),
            correcta = "Lugares y territorios",
            fondo = MPAZAmarilloSuave
        )

        else -> null
    }

    var respuestaSeleccionada by rememberSaveable(asignaturaId) {
        mutableStateOf<String?>(null)
    }

    MPAZBackground {

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {

            val alturaDisponible = maxHeight

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = alturaDisponible)
                    .padding(
                        horizontal = 24.dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    space = 20.dp,
                    alignment = Alignment.CenterVertically
                )
            ) {

                MPAZScreenHeader(
                    titulo = asignatura?.titulo ?: "Asignatura",
                    subtitulo = "¡Vamos a aprender!",
                    onVolverClick = onVolverClick
                )

                if (asignatura == null) {

                    MPAZCard(
                        modifier = Modifier
                            .widthIn(max = 480.dp)
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = "Asignatura no encontrada",
                            color = MPAZTexto
                        )
                    }

                } else {

                    MPAZCard(
                        modifier = Modifier
                            .widthIn(max = 480.dp)
                            .fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment =
                                Alignment.CenterHorizontally,
                            verticalArrangement =
                                Arrangement.spacedBy(14.dp)
                        ) {

                            Text(
                                text = asignatura.emoji,
                                fontSize = 64.sp
                            )

                            Text(
                                text = asignatura.titulo,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MPAZTexto,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = asignatura.descripcion,
                                fontSize = 16.sp,
                                color = MPAZTextoSecundario,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    MPAZCard(
                        modifier = Modifier
                            .widthIn(max = 480.dp)
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = "Actividad de ejemplo",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold,
                            color = MPAZTexto
                        )

                        Spacer(Modifier.height(14.dp))

                        Text(
                            text = asignatura.pregunta,
                            fontSize = 17.sp,
                            color = MPAZTexto
                        )

                        Spacer(Modifier.height(16.dp))

                        asignatura.respuestas.forEach { respuesta ->

                            OutlinedButton(
                                onClick = {
                                    respuestaSeleccionada = respuesta
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Text(
                                    text = respuesta,
                                    fontSize = 16.sp,
                                    color = MPAZTexto
                                )
                            }

                            Spacer(Modifier.height(6.dp))
                        }

                        if (respuestaSeleccionada != null) {

                            val esCorrecta =
                                respuestaSeleccionada ==
                                        asignatura.correcta

                            Spacer(Modifier.height(12.dp))

                            Text(
                                text = if (esCorrecta) {
                                    "¡Muy bien! Respuesta correcta."
                                } else {
                                    "Inténtalo nuevamente."
                                },
                                color = if (esCorrecta) {
                                    MPAZVerde
                                } else {
                                    MPAZError
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                MPAZButton(
                    texto = "Volver a asignaturas",
                    onClick = onVolverClick,
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                )
            }
        }
    }
}

@Preview(
    name = "MPAZ - Matemática centrada",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun AsignaturaDetalleScreenPreview() {

    AppMPAZPRO3_Grupo6Theme {

        AsignaturaDetalleScreen(
            asignaturaId = "matematica"
        )
    }
}