package com.example.appmpazpro3_grupo6.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmpazpro3_grupo6.ui.components.*
import com.example.appmpazpro3_grupo6.ui.theme.*
import com.example.appmpazpro3_grupo6.viewmodel.PerfilImagenViewModel

@Composable
fun PerfilScreen(
    nombre: String = "Estudiante de prueba",
    curso: String = "3° Básico",
    progreso: Float = 0f,
    totalEjerciciosRespondidos: Int = 0,
    progresoAsignaturas: Map<String, Int> = emptyMap(),
    rendimientoGeneral: Int? = null,
    aciertosGeneral: Int = 0,
    ejerciciosEvaluados: Int = 0,
    rendimientoAsignaturas: Map<String, Int?> = emptyMap(),
    mensajeErrorCuenta: String? = null,
    onContinuarClick: () -> Unit = {},
    onMisMetasClick: () -> Unit = {},
    onModoEstudioClick: () -> Unit = {},
    onVolverClick: () -> Unit = {},
    onCerrarSesionClick: () -> Unit = {},
    perfilImagenViewModel: PerfilImagenViewModel = viewModel()
) {
    val imagenUri by perfilImagenViewModel.imagenUri.collectAsState()

    var uriCamaraPendiente by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var errorCamara by remember {
        mutableStateOf<String?>(null)
    }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uriSeleccionada ->
        if (uriSeleccionada != null) {
            perfilImagenViewModel.actualizarDesdeGaleria(
                uriSeleccionada
            )
        }
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { fotografiaTomada ->
        val uri = uriCamaraPendiente?.let {
            Uri.parse(it)
        }

        if (fotografiaTomada && uri != null) {
            perfilImagenViewModel.actualizarDesdeCamara(uri)
            errorCamara = null
        } else {
            perfilImagenViewModel.cancelarCamara()
        }

        uriCamaraPendiente = null
    }

    ContenidoPerfil(
        nombre = nombre,
        curso = curso,
        progreso = progreso,
        totalEjerciciosRespondidos = totalEjerciciosRespondidos,
        progresoAsignaturas = progresoAsignaturas,
        rendimientoGeneral = rendimientoGeneral,
        aciertosGeneral = aciertosGeneral,
        ejerciciosEvaluados = ejerciciosEvaluados,
        rendimientoAsignaturas = rendimientoAsignaturas,
        mensajeErrorCuenta = mensajeErrorCuenta,
        imagenUri = imagenUri,
        onAbrirGaleria = {
            galeriaLauncher.launch("image/*")
        },
        onAbrirCamara = {
            try {
                errorCamara = null

                val uri = perfilImagenViewModel.crearUriCamara()
                uriCamaraPendiente = uri.toString()
                camaraLauncher.launch(uri)
            } catch (e: Exception) {
                uriCamaraPendiente = null
                perfilImagenViewModel.cancelarCamara()
                errorCamara =
                    "No se pudo abrir la cámara. " +
                            "Revisa la configuración de FileProvider."
            }
        },
        errorCamara = errorCamara,
        onContinuarClick = onContinuarClick,
        onMisMetasClick = onMisMetasClick,
        onModoEstudioClick = onModoEstudioClick,
        onVolverClick = onVolverClick,
        onCerrarSesionClick = onCerrarSesionClick
    )
}

@Composable
private fun ContenidoPerfil(
    nombre: String,
    curso: String,
    progreso: Float,
    totalEjerciciosRespondidos: Int,
    progresoAsignaturas: Map<String, Int>,
    rendimientoGeneral: Int?,
    aciertosGeneral: Int,
    ejerciciosEvaluados: Int,
    rendimientoAsignaturas: Map<String, Int?>,
    mensajeErrorCuenta: String?,
    imagenUri: Uri?,
    onAbrirGaleria: () -> Unit,
    onAbrirCamara: () -> Unit,
    errorCamara: String?,
    onContinuarClick: () -> Unit,
    onMisMetasClick: () -> Unit,
    onModoEstudioClick: () -> Unit,
    onVolverClick: () -> Unit,
    onCerrarSesionClick: () -> Unit
) {
    val progresoSeguro = progreso.coerceIn(0f, 1f)

    MPAZBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 24.dp,
                    vertical = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MPAZScreenHeader(
                titulo = "Mi perfil",
                subtitulo = "¡Bienvenido a tu espacio!",
                onVolverClick = onVolverClick
            )

            if (mensajeErrorCuenta != null) {
                Text(
                    text = mensajeErrorCuenta,
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth(),
                    color = MPAZCoral,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            MPAZCard(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ImagenInteligente(
                        imagenUri = imagenUri,
                        modifier = Modifier.size(125.dp)
                    )

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = nombre,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MPAZTexto,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = curso,
                        fontSize = 16.sp,
                        color = MPAZTextoSecundario
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = "Estudiante MPAZ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MPAZAzul
                    )

                    Spacer(Modifier.height(22.dp))

                    MPAZSecondaryButton(
                        texto = "Elegir de galería",
                        onClick = onAbrirGaleria
                    )

                    Spacer(Modifier.height(12.dp))

                    MPAZButton(
                        texto = "Tomar foto",
                        onClick = onAbrirCamara
                    )

                    if (errorCamara != null) {
                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = errorCamara,
                            fontSize = 13.sp,
                            color = MPAZCoral,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(Modifier.height(22.dp))

                Text(
                    text = "🎯 Mi objetivo",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MPAZTexto
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Mi objetivo es aprender algo nuevo cada día, " +
                            "fortalecer mis conocimientos y superar mis propios desafíos. " +
                            "Quiero mejorar en Lenguaje, Matemática, Ciencias Naturales " +
                            "e Historia, practicando con entusiasmo, aprendiendo de mis " +
                            "errores y celebrando cada logro. Con esfuerzo, curiosidad " +
                            "y constancia puedo alcanzar mis metas y seguir creciendo " +
                            "como estudiante.",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = MPAZTextoSecundario
                )
            }

            MPAZCard(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "📊 Mi progreso de aprendizaje",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MPAZTexto
                )

                Spacer(Modifier.height(16.dp))

                MPAZProgress(
                    progreso = progresoSeguro,
                    etiqueta = "Mi progreso general"
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Ejercicios completados: " +
                            "$totalEjerciciosRespondidos de 60",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MPAZTexto
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Este porcentaje muestra cuánto has " +
                            "avanzado, aunque alguna respuesta " +
                            "haya sido incorrecta.",
                    fontSize = 13.sp,
                    color = MPAZTextoSecundario
                )

                Spacer(Modifier.height(22.dp))

                Text(
                    text = "Avance por asignatura",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MPAZTexto
                )

                progresoAsignaturas.forEach { (nombreAsignatura, porcentaje) ->
                    Spacer(Modifier.height(14.dp))

                    MPAZProgress(
                        progreso = porcentaje.coerceIn(0, 100) / 100f,
                        etiqueta = nombreAsignatura
                    )
                }
            }

            MPAZCard(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "🏆 Mis resultados",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MPAZTexto
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Aquí puedes ver cuántas " +
                            "respuestas has acertado.",
                    fontSize = 14.sp,
                    color = MPAZTextoSecundario
                )

                Spacer(Modifier.height(18.dp))

                if (
                    rendimientoGeneral == null ||
                    ejerciciosEvaluados == 0
                ) {
                    Text(
                        text = "Todavía no hay respuestas " +
                                "calificadas guardadas.",
                        fontSize = 15.sp,
                        color = MPAZTextoSecundario
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Completa una actividad para " +
                                "conocer tu rendimiento.",
                        fontSize = 13.sp,
                        color = MPAZTextoSecundario
                    )
                } else {
                    MPAZProgress(
                        progreso = rendimientoGeneral
                            .coerceIn(0, 100) / 100f,
                        etiqueta = "Mi rendimiento general"
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Respuestas correctas: " +
                                "$aciertosGeneral de $ejerciciosEvaluados",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MPAZTexto
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Se considera la última respuesta " +
                                "guardada de cada ejercicio.",
                        fontSize = 13.sp,
                        color = MPAZTextoSecundario
                    )
                }

                Spacer(Modifier.height(22.dp))
                HorizontalDivider()
                Spacer(Modifier.height(18.dp))

                Text(
                    text = "Resultados por asignatura",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MPAZTexto
                )

                rendimientoAsignaturas.forEach { (nombreAsignatura, porcentaje) ->
                    Spacer(Modifier.height(16.dp))

                    if (porcentaje == null) {
                        Text(
                            text = nombreAsignatura,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MPAZTexto
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "Sin respuestas calificadas",
                            fontSize = 13.sp,
                            color = MPAZTextoSecundario
                        )
                    } else {
                        MPAZProgress(
                            progreso = porcentaje.coerceIn(0, 100) / 100f,
                            etiqueta = nombreAsignatura
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "💡 Recuerda: equivocarse también " +
                            "es parte de aprender. Puedes volver " +
                            "a practicar para mejorar tus resultados.",
                    fontSize = 14.sp,
                    color = MPAZTextoSecundario,
                    lineHeight = 21.sp
                )
            }

            MPAZButton(
                texto = "Continuar a asignaturas →",
                onClick = onContinuarClick,
                modifier = Modifier.widthIn(max = 480.dp)
            )

            MPAZSecondaryButton(
                texto = "🎯 Mis metas de estudio",
                onClick = onMisMetasClick,
                modifier = Modifier.widthIn(max = 480.dp)
            )

            MPAZSecondaryButton(
                texto = "Ir a modo estudio",
                onClick = onModoEstudioClick,
                modifier = Modifier.widthIn(max = 480.dp)
            )

            MPAZSecondaryButton(
                texto = "Cerrar sesión",
                onClick = onCerrarSesionClick,
                modifier = Modifier.widthIn(max = 480.dp)
            )

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Preview(
    name = "MPAZ - Perfil y metas",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PerfilScreenPreview() {
    AppMPAZPRO3_Grupo6Theme {
        ContenidoPerfil(
            nombre = "Estudiante de prueba",
            curso = "3° Básico",
            progreso = 0f,
            totalEjerciciosRespondidos = 0,
            progresoAsignaturas = linkedMapOf(
                "Lenguaje y Comunicación" to 0,
                "Matemática" to 0,
                "Ciencias Naturales" to 0,
                "Historia y Geografía" to 0
            ),
            rendimientoGeneral = null,
            aciertosGeneral = 0,
            ejerciciosEvaluados = 0,
            rendimientoAsignaturas = linkedMapOf(
                "Lenguaje y Comunicación" to null,
                "Matemática" to null,
                "Ciencias Naturales" to null,
                "Historia y Geografía" to null
            ),
            mensajeErrorCuenta = null,
            imagenUri = null,
            onAbrirGaleria = {},
            onAbrirCamara = {},
            errorCamara = null,
            onContinuarClick = {},
            onMisMetasClick = {},
            onModoEstudioClick = {},
            onVolverClick = {},
            onCerrarSesionClick = {}
        )
    }
}