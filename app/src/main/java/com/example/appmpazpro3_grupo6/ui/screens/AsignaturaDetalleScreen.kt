package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmpazpro3_grupo6.repository.SesionPractica
import com.example.appmpazpro3_grupo6.ui.components.*
import com.example.appmpazpro3_grupo6.ui.theme.*

private val sesionPracticaSaver = listSaver<SesionPractica, Any>(
    save = {
        listOf(
            it.numeroEjercicio,
            it.respuestaSeleccionada.orEmpty(),
            it.aciertos,
            it.terminado
        )
    },
    restore = {
        SesionPractica(
            numeroEjercicio = it[0] as Int,
            respuestaSeleccionada =
                (it[1] as String).takeIf(String::isNotEmpty),
            aciertos = it[2] as Int,
            terminado = it[3] as Boolean
        )
    }
)

@Composable
fun AsignaturaDetalleScreen(
    asignaturaId: String,
    nivelId: String = "facil",
    onVolverClick: () -> Unit = {},
    onVolverAsignaturasClick: () -> Unit = onVolverClick,
    porcentajeGuardado: Int = 0,
    errorGuardado: String? = null,
    modoEstudioActivo: Boolean = false,
    sesionPractica: SesionPractica? = null,
    guardandoPractica: Boolean = false,
    onCambiarPractica: ((
        SesionPractica,
        SesionPractica,
        Boolean?
    ) -> Unit)? = null
) {

    val asignatura = remember(asignaturaId) {
        datosAsignaturaMPAZ(asignaturaId)
    }

    val dificultad = remember(nivelId) {
        DificultadMPAZ.desdeId(nivelId)
    }

    val ejercicios = remember(asignaturaId, nivelId) {
        dificultad?.let {
            preguntasMPAZ(asignaturaId, it)
        }.orEmpty()
    }

    var sesionLocal by rememberSaveable(
        asignaturaId,
        nivelId,
        stateSaver = sesionPracticaSaver
    ) {
        mutableStateOf(SesionPractica())
    }

    val esperandoDatos =
        onCambiarPractica != null && sesionPractica == null

    val sesion = sesionPractica ?: sesionLocal
    val numero = sesion.numeroEjercicio
    val ejercicio = ejercicios.getOrNull(numero)
    val total = ejercicios.size

    var mostrarPista by rememberSaveable(
        asignaturaId,
        nivelId,
        numero
    ) {
        mutableStateOf(false)
    }

    val ajustes = recordarAjustesAccesibilidadMPAZ()

    var inclusionActiva by rememberSaveable(
        asignaturaId,
        nivelId
    ) {
        mutableStateOf(false)
    }

    val textoGrandeActivo =
        inclusionActiva && ajustes.textoGrande

    val contrasteActivo =
        inclusionActiva && ajustes.altoContraste

    val botonesGrandesActivos =
        inclusionActiva && ajustes.botonesGrandes

    val densidadActual = LocalDensity.current

    val densidadAdaptada = remember(
        densidadActual,
        textoGrandeActivo
    ) {
        Density(
            density = densidadActual.density,
            fontScale = densidadActual.fontScale *
                    if (textoGrandeActivo) 1.25f else 1f
        )
    }

    val fondoPregunta = if (contrasteActivo) {
        Color(0xFF17263D)
    } else {
        MPAZBlanco
    }

    val textoPregunta = if (contrasteActivo) {
        Color.White
    } else {
        MPAZTexto
    }

    val textoAyuda = if (contrasteActivo) {
        Color(0xFFE2E8F0)
    } else {
        MPAZTextoSecundario
    }

    fun guardar(
        siguiente: SesionPractica,
        esCorrecta: Boolean? = null
    ) {
        if (guardandoPractica) return

        if (onCambiarPractica != null) {
            onCambiarPractica(
                sesion,
                siguiente,
                esCorrecta
            )
        } else {
            sesionLocal = siguiente
        }
    }

    fun responder(opcion: String) {

        if (
            sesion.respuestaSeleccionada != null ||
            guardandoPractica
        ) {
            return
        }

        val acierto = opcion == ejercicio?.correcta

        guardar(
            sesion.copy(
                respuestaSeleccionada = opcion,
                aciertos = sesion.aciertos +
                        if (acierto) 1 else 0
            ),
            esCorrecta = acierto
        )
    }

    val porcentajeVisible = when {

        onCambiarPractica != null ->
            porcentajeGuardado.coerceIn(0, 100)

        sesion.terminado ->
            100

        else ->
            (
                    (
                            numero +
                                    if (
                                        sesion.respuestaSeleccionada != null
                                    ) 1 else 0
                            ) * 20
                    ).coerceIn(0, 100)
    }

    CompositionLocalProvider(
        LocalDensity provides densidadAdaptada
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
                                horizontal = 20.dp,
                                vertical = 24.dp
                            ),
                        horizontalAlignment =
                            Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Column(
                            modifier = Modifier
                                .widthIn(max = 500.dp)
                                .fillMaxWidth(),
                            horizontalAlignment =
                                Alignment.CenterHorizontally,
                            verticalArrangement =
                                Arrangement.spacedBy(15.dp)
                        ) {

                            when {

                                esperandoDatos -> {

                                    Text(
                                        text = "Cargando práctica…",
                                        color = MPAZTexto
                                    )
                                }

                                asignatura == null ||
                                        dificultad == null ||
                                        total != 5 -> {

                                    MPAZCard(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {

                                        Text(
                                            text = "No se encontraron los ejercicios de este nivel.",
                                            modifier = Modifier.fillMaxWidth(),
                                            color = MPAZTexto,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                sesion.terminado -> {

                                    Text(
                                        text = asignatura.titulo,
                                        modifier = Modifier.fillMaxWidth(),
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MPAZTexto,
                                        textAlign = TextAlign.Center
                                    )

                                    MPAZCard(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {

                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment =
                                                Alignment.CenterHorizontally,
                                            verticalArrangement =
                                                Arrangement.spacedBy(13.dp)
                                        ) {

                                            Text(
                                                text = "🦉🏆",
                                                fontSize = 55.sp
                                            )

                                            Text(
                                                text = "¡Felicidades!",
                                                fontSize = 26.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MPAZTexto,
                                                textAlign = TextAlign.Center
                                            )

                                            Text(
                                                text = "Has completado los ejercicios.",
                                                fontSize = 17.sp,
                                                color = MPAZTexto,
                                                textAlign = TextAlign.Center
                                            )

                                            Text(
                                                text = "Nivel ${dificultad.titulo} · 5 de 5 preguntas",
                                                color = MPAZTextoSecundario,
                                                textAlign = TextAlign.Center
                                            )

                                            Text(
                                                text = "Respuestas correctas: ${sesion.aciertos} de 5",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MPAZVerde,
                                                textAlign = TextAlign.Center
                                            )

                                            Text(
                                                text = "Progreso del nivel: $porcentajeVisible%",
                                                color = MPAZTextoSecundario,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }

                                    MPAZButton(
                                        texto = "Repetir nivel",
                                        onClick = {
                                            guardar(SesionPractica())
                                        },
                                        enabled = !guardandoPractica
                                    )

                                    MPAZButton(
                                        texto = "Elegir otra dificultad",
                                        onClick = onVolverClick
                                    )

                                    Spacer(
                                        modifier = Modifier.height(3.dp)
                                    )

                                    Button(
                                        onClick = onVolverAsignaturasClick,
                                        modifier = Modifier.size(70.dp),
                                        shape = CircleShape,
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MPAZCoral
                                        )
                                    ) {

                                        Text(
                                            text = "⌂",
                                            fontSize = 34.sp,
                                            color = MPAZBlanco
                                        )
                                    }

                                    Text(
                                        text = "Volver a las asignaturas",
                                        modifier = Modifier.fillMaxWidth(),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MPAZTexto,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                ejercicio != null -> {

                                    Text(
                                        text = asignatura.titulo,
                                        modifier = Modifier.fillMaxWidth(),
                                        fontSize = 27.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MPAZTexto,
                                        textAlign = TextAlign.Center
                                    )

                                    Text(
                                        text = "¡Vamos a aprender!",
                                        modifier = Modifier.fillMaxWidth(),
                                        color = MPAZTextoSecundario,
                                        textAlign = TextAlign.Center
                                    )

                                    if (modoEstudioActivo) {

                                        Text(
                                            text = "📚 Modo estudio activado",
                                            modifier = Modifier.fillMaxWidth(),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MPAZVerde,
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                    TextButton(
                                        onClick = {
                                            inclusionActiva = !inclusionActiva
                                        }
                                    ) {

                                        Text(
                                            text = if (inclusionActiva) {
                                                "♿ Desactivar inclusión"
                                            } else {
                                                "♿ Activar inclusión"
                                            },
                                            fontWeight = FontWeight.Bold,
                                            color = MPAZCoral,
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                    if (inclusionActiva) {

                                        ConfiguracionAccesibilidadMPAZ(
                                            ajustes = ajustes
                                        )
                                    }

                                    MPAZCard(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {

                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment =
                                                Alignment.CenterHorizontally,
                                            verticalArrangement =
                                                Arrangement.spacedBy(10.dp)
                                        ) {

                                            Box(
                                                modifier = Modifier
                                                    .size(70.dp)
                                                    .background(
                                                        MPAZAzulSuave,
                                                        RoundedCornerShape(14.dp)
                                                    ),
                                                contentAlignment =
                                                    Alignment.Center
                                            ) {

                                                Text(
                                                    text = asignatura.emoji,
                                                    fontSize = 36.sp
                                                )
                                            }

                                            Text(
                                                text = asignatura.titulo,
                                                color = MPAZTexto,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            )

                                            Text(
                                                text = asignatura.descripcion,
                                                modifier = Modifier.fillMaxWidth(),
                                                color = MPAZTextoSecundario,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }

                                    MPAZCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        fondo = fondoPregunta
                                    ) {

                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment =
                                                Alignment.CenterHorizontally,
                                            verticalArrangement =
                                                Arrangement.spacedBy(12.dp)
                                        ) {

                                            Text(
                                                text = "${asignatura.emoji} ${dificultad.estrellas} ${dificultad.titulo}",
                                                modifier = Modifier.fillMaxWidth(),
                                                color = textoPregunta,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            )

                                            Text(
                                                text = "Ejercicio ${numero + 1} de 5",
                                                color = textoPregunta,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp
                                            )

                                            Text(
                                                text = "Avance guardado: $porcentajeVisible%",
                                                color = textoAyuda,
                                                fontSize = 14.sp
                                            )

                                            LinearProgressIndicator(
                                                progress = {
                                                    porcentajeVisible / 100f
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                color = if (contrasteActivo) {
                                                    Color(0xFFFFD166)
                                                } else {
                                                    MPAZCoral
                                                },
                                                trackColor = if (contrasteActivo) {
                                                    Color(0xFF53657B)
                                                } else {
                                                    MPAZAmarilloSuave
                                                }
                                            )

                                            Text(
                                                text = ejercicio.pregunta,
                                                modifier = Modifier.fillMaxWidth(),
                                                fontSize = 18.sp,
                                                color = textoPregunta,
                                                textAlign = TextAlign.Center
                                            )

                                            if (inclusionActiva) {

                                                LectorPreguntasMPAZ(
                                                    pregunta = ejercicio.pregunta,
                                                    opciones = ejercicio.opciones,
                                                    explicacion =
                                                        ejercicio.explicacion.takeIf {
                                                            sesion.respuestaSeleccionada != null
                                                        },
                                                    respuestaCorrecta =
                                                        ejercicio.correcta.takeIf {
                                                            sesion.respuestaSeleccionada != null
                                                        },
                                                    clavePregunta =
                                                        "$asignaturaId:$nivelId:$numero"
                                                )

                                                if (LocalInspectionMode.current) {

                                                    Text(
                                                        text = "🎤 Responder por voz",
                                                        color = textoAyuda,
                                                        textAlign = TextAlign.Center
                                                    )

                                                } else {

                                                    RespuestaPorVozMPAZ(
                                                        opciones = ejercicio.opciones,
                                                        habilitado =
                                                            sesion.respuestaSeleccionada == null &&
                                                                    !guardandoPractica,
                                                        clavePregunta =
                                                            "$asignaturaId:$nivelId:$numero",
                                                        onRespuesta = { opcion ->
                                                            responder(opcion)
                                                        }
                                                    )
                                                }
                                            }

                                            if (
                                                modoEstudioActivo &&
                                                sesion.respuestaSeleccionada == null
                                            ) {

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
                                                        }
                                                    )
                                                }

                                                AnimatedVisibility(
                                                    visible = mostrarPista
                                                ) {

                                                    Text(
                                                        text = ejercicio.pista,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        color = textoAyuda,
                                                        textAlign = TextAlign.Center
                                                    )
                                                }
                                            }

                                            ejercicio.opciones.forEach { opcion ->

                                                val respondida =
                                                    sesion.respuestaSeleccionada != null

                                                val correcta =
                                                    opcion == ejercicio.correcta

                                                val elegida =
                                                    opcion == sesion.respuestaSeleccionada

                                                val borde = when {

                                                    respondida && correcta ->
                                                        if (contrasteActivo) {
                                                            Color(0xFF86EFAC)
                                                        } else {
                                                            MPAZVerde
                                                        }

                                                    respondida && elegida ->
                                                        if (contrasteActivo) {
                                                            Color(0xFFFCA5A5)
                                                        } else {
                                                            MPAZError
                                                        }

                                                    else ->
                                                        if (contrasteActivo) {
                                                            Color.White
                                                        } else {
                                                            Color.LightGray
                                                        }
                                                }

                                                OutlinedButton(
                                                    onClick = {
                                                        responder(opcion)
                                                    },
                                                    enabled =
                                                        !respondida &&
                                                                !guardandoPractica,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .heightIn(
                                                            min =
                                                                if (botonesGrandesActivos) {
                                                                    72.dp
                                                                } else {
                                                                    48.dp
                                                                }
                                                        ),
                                                    border = BorderStroke(
                                                        if (respondida) {
                                                            2.dp
                                                        } else {
                                                            1.dp
                                                        },
                                                        borde
                                                    ),
                                                    colors =
                                                        ButtonDefaults.outlinedButtonColors(
                                                            containerColor =
                                                                if (contrasteActivo) {
                                                                    Color(0xFF273D55)
                                                                } else {
                                                                    Color.Transparent
                                                                },
                                                            disabledContainerColor =
                                                                if (contrasteActivo) {
                                                                    Color(0xFF273D55)
                                                                } else {
                                                                    Color.Transparent
                                                                }
                                                        )
                                                ) {

                                                    Text(
                                                        text = opcion,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        color = textoPregunta,
                                                        textAlign = TextAlign.Center
                                                    )
                                                }
                                            }

                                            AnimatedVisibility(
                                                visible =
                                                    sesion.respuestaSeleccionada != null
                                            ) {

                                                Column(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalAlignment =
                                                        Alignment.CenterHorizontally,
                                                    verticalArrangement =
                                                        Arrangement.spacedBy(8.dp)
                                                ) {

                                                    val acierto =
                                                        sesion.respuestaSeleccionada ==
                                                                ejercicio.correcta

                                                    Text(
                                                        text = if (acierto) {
                                                            "✅ ¡Muy bien! Respuesta correcta."
                                                        } else {
                                                            "💡 Respuesta correcta: ${ejercicio.correcta}"
                                                        },
                                                        modifier = Modifier.fillMaxWidth(),
                                                        color = if (acierto) {
                                                            if (contrasteActivo) {
                                                                Color(0xFF86EFAC)
                                                            } else {
                                                                MPAZVerde
                                                            }
                                                        } else {
                                                            if (contrasteActivo) {
                                                                Color(0xFFFCA5A5)
                                                            } else {
                                                                MPAZError
                                                            }
                                                        },
                                                        fontWeight = FontWeight.Bold,
                                                        textAlign = TextAlign.Center
                                                    )

                                                    Text(
                                                        text = ejercicio.explicacion,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        color = textoPregunta,
                                                        textAlign = TextAlign.Center
                                                    )

                                                    if (modoEstudioActivo) {

                                                        Text(
                                                            text = "📚 Consejo: ${ejercicio.consejo}",
                                                            modifier = Modifier.fillMaxWidth(),
                                                            color = textoAyuda,
                                                            textAlign = TextAlign.Center
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (
                                        sesion.respuestaSeleccionada != null
                                    ) {

                                        MPAZLecturaCiencias(
                                            articulo = ejercicio.articuloWikipedia,
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        MPAZButton(
                                            texto = if (numero == total - 1) {
                                                "Ver mis resultados →"
                                            } else {
                                                "Siguiente pregunta →"
                                            },
                                            onClick = {

                                                val siguiente =
                                                    if (numero == total - 1) {

                                                        sesion.copy(
                                                            terminado = true
                                                        )

                                                    } else {

                                                        sesion.copy(
                                                            numeroEjercicio = numero + 1,
                                                            respuestaSeleccionada = null
                                                        )
                                                    }

                                                guardar(siguiente)
                                            },
                                            enabled = !guardandoPractica
                                        )
                                    }
                                }

                                else -> {

                                    Text(
                                        text = "No se pudo recuperar esta pregunta.",
                                        modifier = Modifier.fillMaxWidth(),
                                        color = MPAZError,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            if (errorGuardado != null) {

                                Text(
                                    text = errorGuardado,
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MPAZError,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Matemática - Fácil",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
private fun AsignaturaDetalleScreenPreview() {

    AppMPAZPRO3_Grupo6Theme {

        AsignaturaDetalleScreen(
            asignaturaId = "matematica",
            nivelId = "facil"
        )
    }
}

@Preview(
    name = "Matemática - Terminada",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
private fun AsignaturaTerminadaPreview() {

    AppMPAZPRO3_Grupo6Theme {

        AsignaturaDetalleScreen(
            asignaturaId = "matematica",
            nivelId = "facil",
            sesionPractica = SesionPractica(
                numeroEjercicio = 4,
                aciertos = 4,
                terminado = true
            ),
            porcentajeGuardado = 100
        )
    }
}