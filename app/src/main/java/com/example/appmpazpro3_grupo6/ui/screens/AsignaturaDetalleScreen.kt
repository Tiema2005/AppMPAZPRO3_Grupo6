package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.appmpazpro3_grupo6.ui.components.*
import com.example.appmpazpro3_grupo6.ui.theme.*

private data class Ejercicio(
    val nivel: String,
    val pregunta: String,
    val opciones: List<String>,
    val correcta: String,
    val explicacion: String
)

private data class ContenidoAsignatura(
    val titulo: String,
    val emoji: String,
    val descripcion: String,
    val ejercicios: List<Ejercicio>
)

private fun obtenerAsignatura(id: String): ContenidoAsignatura? {
    return when (id) {

        "lenguaje" -> ContenidoAsignatura(
            titulo = "Lenguaje y Comunicación",
            emoji = "📖",
            descripcion = "Aprenderemos a leer, escribir y comprender textos.",
            ejercicios = listOf(
                Ejercicio(
                    nivel = "Fácil",
                    pregunta = "¿Cuál de estas palabras es un sustantivo?",
                    opciones = listOf("Correr", "Mesa", "Rápido"),
                    correcta = "Mesa",
                    explicacion = "Mesa es un sustantivo porque nombra un " +
                            "objeto. Correr es un verbo y rápido puede " +
                            "ser un adjetivo."
                ),
                Ejercicio(
                    nivel = "Medio",
                    pregunta = "¿Qué oración contiene un adjetivo?",
                    opciones = listOf(
                        "El gato duerme.",
                        "El gato negro duerme.",
                        "El gato corre."
                    ),
                    correcta = "El gato negro duerme.",
                    explicacion = "La palabra negro es un adjetivo porque " +
                            "describe una característica del gato."
                ),
                Ejercicio(
                    nivel = "Difícil",
                    pregunta = "Lucía llevó un paraguas porque había " +
                            "nubes oscuras. ¿Qué podemos inferir?",
                    opciones = listOf(
                        "Podría llover.",
                        "Hace mucho calor.",
                        "Es de noche."
                    ),
                    correcta = "Podría llover.",
                    explicacion = "Inferir significa utilizar pistas. " +
                            "El paraguas y las nubes oscuras indican " +
                            "que probablemente va a llover."
                )
            )
        )

        "matematica" -> ContenidoAsignatura(
            titulo = "Matemática",
            emoji = "🔢",
            descripcion = "Aprenderemos a calcular y resolver problemas.",
            ejercicios = listOf(
                Ejercicio(
                    nivel = "Fácil",
                    pregunta = "¿Cuánto es 7 + 5?",
                    opciones = listOf("10", "12", "14"),
                    correcta = "12",
                    explicacion = "Partimos de 7 y sumamos 5: " +
                            "8, 9, 10, 11 y 12. Por eso 7 + 5 = 12."
                ),
                Ejercicio(
                    nivel = "Medio",
                    pregunta = "¿Cuánto es 23 + 18?",
                    opciones = listOf("31", "41", "51"),
                    correcta = "41",
                    explicacion = "Sumamos las unidades: 3 + 8 = 11. " +
                            "Escribimos 1 y llevamos 1 decena. " +
                            "Luego sumamos las decenas: " +
                            "2 + 1 + 1 = 4. Resultado: 41."
                ),
                Ejercicio(
                    nivel = "Difícil",
                    pregunta = "Una biblioteca tiene 46 libros, " +
                            "presta 19 y recibe 28 nuevos. " +
                            "¿Cuántos libros tiene al final?",
                    opciones = listOf("55", "65", "37"),
                    correcta = "55",
                    explicacion = "Primero restamos los prestados: " +
                            "46 - 19 = 27. Después sumamos los nuevos: " +
                            "27 + 28 = 55 libros."
                )
            )
        )

        "ciencias" -> ContenidoAsignatura(
            titulo = "Ciencias Naturales",
            emoji = "🧪",
            descripcion = "Descubriremos los seres vivos y la naturaleza.",
            ejercicios = listOf(
                Ejercicio(
                    nivel = "Fácil",
                    pregunta = "¿Qué necesitan las plantas para crecer?",
                    opciones = listOf(
                        "Luz y agua",
                        "Solo piedras",
                        "Solo plástico"
                    ),
                    correcta = "Luz y agua",
                    explicacion = "Las plantas necesitan luz y agua " +
                            "para crecer. También necesitan aire, " +
                            "nutrientes y condiciones adecuadas."
                ),
                Ejercicio(
                    nivel = "Medio",
                    pregunta = "¿Qué parte de la planta absorbe " +
                            "agua del suelo?",
                    opciones = listOf(
                        "Las flores",
                        "La raíz",
                        "Los frutos"
                    ),
                    correcta = "La raíz",
                    explicacion = "La raíz absorbe el agua y los " +
                            "minerales del suelo que necesita la planta."
                ),
                Ejercicio(
                    nivel = "Difícil",
                    pregunta = "Cuando el agua se calienta y " +
                            "se transforma en vapor, ¿cómo se " +
                            "llama ese proceso?",
                    opciones = listOf(
                        "Congelación",
                        "Evaporación",
                        "Condensación"
                    ),
                    correcta = "Evaporación",
                    explicacion = "La evaporación transforma el " +
                            "agua líquida en vapor. La congelación " +
                            "forma hielo y la condensación transforma " +
                            "vapor en gotas de agua."
                )
            )
        )

        "historia" -> ContenidoAsignatura(
            titulo = "Historia y Geografía",
            emoji = "🌎",
            descripcion = "Conoceremos mapas, lugares y nuestro entorno.",
            ejercicios = listOf(
                Ejercicio(
                    nivel = "Fácil",
                    pregunta = "¿Qué representa un mapa?",
                    opciones = listOf(
                        "Lugares y territorios",
                        "Recetas",
                        "Canciones"
                    ),
                    correcta = "Lugares y territorios",
                    explicacion = "Un mapa representa lugares y " +
                            "territorios mediante símbolos, colores " +
                            "y ubicaciones."
                ),
                Ejercicio(
                    nivel = "Medio",
                    pregunta = "Si el norte está arriba en un mapa, " +
                            "¿dónde está el este?",
                    opciones = listOf(
                        "A la izquierda",
                        "A la derecha",
                        "Abajo"
                    ),
                    correcta = "A la derecha",
                    explicacion = "En un mapa con el norte arriba, " +
                            "el sur está abajo, el este a la derecha " +
                            "y el oeste a la izquierda."
                ),
                Ejercicio(
                    nivel = "Difícil",
                    pregunta = "Caminas dos cuadras al norte y " +
                            "una al este. ¿En qué dirección general " +
                            "quedas respecto del inicio?",
                    opciones = listOf(
                        "Suroeste",
                        "Noreste",
                        "Noroeste"
                    ),
                    correcta = "Noreste",
                    explicacion = "Como avanzaste hacia el norte " +
                            "y hacia el este, quedas al noreste " +
                            "de tu punto de partida."
                )
            )
        )

        else -> null
    }
}

@Composable
fun AsignaturaDetalleScreen(
    asignaturaId: String,
    onVolverClick: () -> Unit = {},
    porcentajeGuardado: Int = 0,
    errorGuardado: String? = null,
    onEjercicioRespondido: (Int) -> Unit = {}
) {
    val asignatura = remember(asignaturaId) {
        obtenerAsignatura(asignaturaId)
    }

    var numeroEjercicio by rememberSaveable(asignaturaId) {
        mutableIntStateOf(0)
    }

    var respuestaSeleccionada by rememberSaveable(asignaturaId) {
        mutableStateOf<String?>(null)
    }

    var aciertos by rememberSaveable(asignaturaId) {
        mutableIntStateOf(0)
    }

    var terminado by rememberSaveable(asignaturaId) {
        mutableStateOf(false)
    }

    val ejercicios = asignatura?.ejercicios.orEmpty()
    val ejercicio = ejercicios.getOrNull(numeroEjercicio)
    val total = ejercicios.size

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
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    space = 20.dp,
                    alignment = Alignment.CenterVertically
                )
            ) {

                MPAZScreenHeader(
                    titulo = asignatura?.titulo ?: "Asignatura",
                    subtitulo = "¡Vamos a aprender!"
                )

                if (asignatura == null || ejercicio == null) {

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

                } else if (terminado) {

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
                            Text("🏆", fontSize = 55.sp)

                            Text(
                                text = "¡Terminaste los ejercicios!",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MPAZTexto,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "Avance guardado: $porcentajeGuardado%",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MPAZVerde
                            )

                            Text(
                                text = "Respuestas correctas: $aciertos de $total",
                                fontSize = 17.sp,
                                color = MPAZTexto
                            )

                            Text(
                                text = "¡Llegaste al nivel difícil!",
                                color = MPAZTextoSecundario,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    MPAZButton(
                        texto = "Volver a practicar",
                        onClick = {
                            numeroEjercicio = 0
                            respuestaSeleccionada = null
                            aciertos = 0
                            terminado = false
                        },
                        modifier = Modifier.widthIn(max = 480.dp)
                    )

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
                                Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = asignatura.emoji,
                                fontSize = 52.sp
                            )

                            Text(
                                text = asignatura.titulo,
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold,
                                color = MPAZTexto,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = asignatura.descripcion,
                                fontSize = 15.sp,
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
                            text = "Ejercicio ${numeroEjercicio + 1} " +
                                    "de $total · Nivel ${ejercicio.nivel}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MPAZTexto
                        )

                        Spacer(Modifier.height(14.dp))

                        Text(
                            text = "Avance guardado: $porcentajeGuardado%",
                            fontSize = 14.sp,
                            color = MPAZTextoSecundario
                        )

                        Spacer(Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = {
                                (porcentajeGuardado / 100f)
                                    .coerceIn(0f, 1f)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            color = MPAZCoral,
                            trackColor = MPAZAmarilloSuave
                        )

                        Spacer(Modifier.height(18.dp))

                        Text(
                            text = ejercicio.pregunta,
                            fontSize = 17.sp,
                            color = MPAZTexto
                        )

                        Spacer(Modifier.height(14.dp))

                        ejercicio.opciones.forEach { opcion ->

                            val hayRespuesta =
                                respuestaSeleccionada != null

                            val correcta =
                                opcion == ejercicio.correcta

                            val elegida =
                                opcion == respuestaSeleccionada

                            val colorBorde = when {
                                hayRespuesta && correcta -> MPAZVerde
                                hayRespuesta && elegida -> MPAZError
                                else -> Color.LightGray
                            }

                            OutlinedButton(
                                onClick = {
                                    if (respuestaSeleccionada == null) {

                                        respuestaSeleccionada = opcion

                                        onEjercicioRespondido(
                                            numeroEjercicio + 1
                                        )

                                        if (correcta) {
                                            aciertos++
                                        }
                                    }
                                },
                                enabled = !hayRespuesta,
                                border = BorderStroke(
                                    width = if (
                                        hayRespuesta &&
                                        (correcta || elegida)
                                    ) 2.dp else 1.dp,
                                    color = colorBorde
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = opcion,
                                    fontSize = 16.sp,
                                    color = MPAZTexto,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(Modifier.height(5.dp))
                        }

                        AnimatedVisibility(
                            visible = respuestaSeleccionada != null
                        ) {
                            Column {

                                Spacer(Modifier.height(14.dp))

                                val esCorrecta =
                                    respuestaSeleccionada ==
                                            ejercicio.correcta

                                Text(
                                    text = if (esCorrecta) {
                                        "✅ ¡Muy bien! Respuesta correcta."
                                    } else {
                                        "💡 Respuesta correcta: " +
                                                ejercicio.correcta
                                    },
                                    color = if (esCorrecta) {
                                        MPAZVerde
                                    } else {
                                        MPAZError
                                    },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(Modifier.height(10.dp))

                                Text(
                                    text = ejercicio.explicacion,
                                    fontSize = 15.sp,
                                    lineHeight = 23.sp,
                                    color = MPAZTexto
                                )
                            }
                        }
                    }

                    if (errorGuardado != null) {
                        Text(
                            text = errorGuardado,
                            color = MPAZError,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    if (respuestaSeleccionada != null) {

                        MPAZButton(
                            texto = if (numeroEjercicio == total - 1) {
                                "Ver mis resultados →"
                            } else {
                                "Siguiente ejercicio →"
                            },
                            onClick = {
                                if (numeroEjercicio == total - 1) {
                                    terminado = true
                                } else {
                                    numeroEjercicio++
                                    respuestaSeleccionada = null
                                }
                            },
                            modifier = Modifier.widthIn(max = 480.dp)
                        )
                    }
                }

                MPAZButton(
                    texto = "Volver a asignaturas",
                    onClick = onVolverClick,
                    modifier = Modifier.widthIn(max = 480.dp)
                )
            }
        }
    }
}

@Preview(
    name = "MPAZ - Matemática con progreso real",
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