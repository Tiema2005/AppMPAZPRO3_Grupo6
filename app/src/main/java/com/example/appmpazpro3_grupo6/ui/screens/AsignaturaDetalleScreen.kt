package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
    val explicacion: String,
    val pista: String,
    val consejo: String
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
                    explicacion = "Mesa es un sustantivo porque nombra " +
                            "un objeto. Correr es un verbo y rápido " +
                            "puede ser un adjetivo.",
                    pista = "Piensa cuál de las palabras nombra un objeto.",
                    consejo = "Para reconocer un sustantivo, pregunta: " +
                            "¿es el nombre de una persona, animal, " +
                            "lugar o cosa?"
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
                    explicacion = "La palabra negro es un adjetivo " +
                            "porque describe una característica del gato.",
                    pista = "Busca una palabra que describa cómo es el gato.",
                    consejo = "Los adjetivos permiten describir " +
                            "colores, tamaños y otras características."
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
                    explicacion = "El paraguas y las nubes oscuras " +
                            "son pistas que indican que podría llover.",
                    pista = "Relaciona el paraguas con el estado del cielo.",
                    consejo = "Inferir es descubrir una idea usando " +
                            "las pistas del texto, aunque no " +
                            "se diga directamente."
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
                            "8, 9, 10, 11 y 12. Por eso 7 + 5 = 12.",
                    pista = "Empieza en 7 y cuenta cinco números más.",
                    consejo = "Puedes usar tus dedos o dibujar " +
                            "elementos para practicar las sumas."
                ),
                Ejercicio(
                    nivel = "Medio",
                    pregunta = "¿Cuánto es 23 + 18?",
                    opciones = listOf("31", "41", "51"),
                    correcta = "41",
                    explicacion = "Sumamos las unidades: 3 + 8 = 11. " +
                            "Escribimos 1 y llevamos 1 decena. " +
                            "Después sumamos 2 + 1 + 1 = 4 decenas. " +
                            "El resultado es 41.",
                    pista = "Suma las unidades 3 + 8. " +
                            "Recuerda llevar una decena.",
                    consejo = "Ordena los números en columnas: " +
                            "unidades con unidades y decenas con decenas."
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
                            "27 + 28 = 55 libros.",
                    pista = "Primero resta los libros prestados. " +
                            "Después suma los libros recibidos.",
                    consejo = "En problemas de varias operaciones, " +
                            "separa los pasos. Identifica qué cantidad " +
                            "disminuye y cuál aumenta."
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
                            "para crecer. También requieren aire, " +
                            "nutrientes y condiciones adecuadas.",
                    pista = "Piensa qué necesita una planta " +
                            "cuando la riegas y la dejas cerca del sol.",
                    consejo = "Puedes observar una planta en casa " +
                            "y registrar cómo cambia con los días."
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
                    explicacion = "La raíz absorbe agua y minerales " +
                            "del suelo que la planta necesita.",
                    pista = "Busca la parte que normalmente " +
                            "está debajo de la tierra.",
                    consejo = "Dibuja una planta y escribe " +
                            "el nombre de sus partes principales."
                ),
                Ejercicio(
                    nivel = "Difícil",
                    pregunta = "Cuando el agua se calienta y se " +
                            "transforma en vapor, ¿cómo se llama " +
                            "ese proceso?",
                    opciones = listOf(
                        "Congelación",
                        "Evaporación",
                        "Condensación"
                    ),
                    correcta = "Evaporación",
                    explicacion = "La evaporación transforma " +
                            "el agua líquida en vapor. La congelación " +
                            "forma hielo y la condensación transforma " +
                            "vapor en gotas de agua.",
                    pista = "Piensa qué ocurre con un charco " +
                            "cuando recibe calor durante el día.",
                    consejo = "Recuerda los cambios de estado: " +
                            "sólido, líquido y gaseoso."
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
                    explicacion = "Un mapa representa lugares " +
                            "y territorios mediante símbolos, " +
                            "colores y ubicaciones.",
                    pista = "Piensa qué utilizamos para encontrar lugares.",
                    consejo = "Revisa el título, los símbolos y " +
                            "la orientación cuando observes un mapa."
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
                    explicacion = "Cuando el norte está arriba, " +
                            "el sur está abajo, el este a la derecha " +
                            "y el oeste a la izquierda.",
                    pista = "Imagina una rosa de los vientos " +
                            "con el norte apuntando hacia arriba.",
                    consejo = "Practica con una rosa de los vientos: " +
                            "Norte, Sur, Este y Oeste."
                ),
                Ejercicio(
                    nivel = "Difícil",
                    pregunta = "Caminas dos cuadras al norte " +
                            "y una al este. ¿En qué dirección general " +
                            "quedas respecto del inicio?",
                    opciones = listOf(
                        "Suroeste",
                        "Noreste",
                        "Noroeste"
                    ),
                    correcta = "Noreste",
                    explicacion = "Como avanzaste hacia el norte " +
                            "y hacia el este, quedas al noreste " +
                            "de tu punto de partida.",
                    pista = "Combina las dos direcciones " +
                            "en las que caminaste.",
                    consejo = "Dibuja una cruz con los puntos cardinales " +
                            "y marca cada movimiento con una flecha."
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
    onEjercicioRespondido: (Int) -> Unit = {},

    modoEstudioActivo: Boolean = false
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

    var mostrarPista by rememberSaveable(asignaturaId) {
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
                    subtitulo = "¡Vamos a aprender!"
                )

                if (modoEstudioActivo) {
                    Text(
                        text = "📚 Modo estudio activado",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MPAZVerde,
                        textAlign = TextAlign.Center
                    )
                }

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
                            Text(
                                text = "🏆",
                                fontSize = 55.sp
                            )

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
                                text = "Respuestas correctas: " +
                                        "$aciertos de $total",
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
                            mostrarPista = false
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

                        if (
                            modoEstudioActivo &&
                            respuestaSeleccionada == null
                        ) {
                            Spacer(Modifier.height(10.dp))

                            TextButton(
                                onClick = {
                                    mostrarPista = !mostrarPista
                                }
                            ) {
                                Text(
                                    text = if (mostrarPista) {
                                        "Ocultar pista ▲"
                                    } else {
                                        "💡 Ver una pista"
                                    },
                                    color = MPAZCoral,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            AnimatedVisibility(
                                visible = mostrarPista
                            ) {
                                Column {
                                    Spacer(Modifier.height(6.dp))

                                    Text(
                                        text = "💡 " + ejercicio.pista,
                                        fontSize = 15.sp,
                                        lineHeight = 22.sp,
                                        color = MPAZTextoSecundario
                                    )

                                    Spacer(Modifier.height(8.dp))
                                }
                            }
                        }

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
                                    if (
                                        respuestaSeleccionada == null
                                    ) {
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
                                    ) {
                                        2.dp
                                    } else {
                                        1.dp
                                    },
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

                                if (modoEstudioActivo) {
                                    Spacer(Modifier.height(14.dp))

                                    Text(
                                        text = "📚 Consejo para aprender",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MPAZVerde
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    Text(
                                        text = ejercicio.consejo,
                                        fontSize = 15.sp,
                                        lineHeight = 23.sp,
                                        color = MPAZTextoSecundario
                                    )
                                }
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
                            texto = if (
                                numeroEjercicio == total - 1
                            ) {
                                "Ver mis resultados →"
                            } else {
                                "Siguiente ejercicio →"
                            },
                            onClick = {
                                if (
                                    numeroEjercicio == total - 1
                                ) {
                                    terminado = true
                                } else {
                                    numeroEjercicio++
                                    respuestaSeleccionada = null
                                    mostrarPista = false
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
    name = "MPAZ - Matemática modo normal",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun AsignaturaDetalleScreenPreview() {
    AppMPAZPRO3_Grupo6Theme {
        AsignaturaDetalleScreen(
            asignaturaId = "matematica",
            modoEstudioActivo = false
        )
    }
}

@Preview(
    name = "MPAZ - Matemática modo estudio",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun AsignaturaModoEstudioPreview() {
    AppMPAZPRO3_Grupo6Theme {
        AsignaturaDetalleScreen(
            asignaturaId = "matematica",
            modoEstudioActivo = true
        )
    }
}