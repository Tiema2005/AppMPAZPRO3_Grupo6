package com.example.appmpazpro3_grupo6.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    progreso: Float = 0.6f,
    onContinuarClick: () -> Unit = {},
    onModoEstudioClick: () -> Unit = {},
    onVolverClick: () -> Unit = {},
    perfilImagenViewModel: PerfilImagenViewModel = viewModel()
) {
    // Estado de la imagen desde el ViewModel
    val imagenUri by perfilImagenViewModel
        .imagenUri.collectAsState()

    // Conservar la URI pendiente al recrearse la pantalla
    var uriCamaraPendiente by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var errorCamara by remember {
        mutableStateOf<String?>(null)
    }

    // GALERÍA: seleccionar una fotografía existente
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uriSeleccionada ->

        if (uriSeleccionada != null) {
            perfilImagenViewModel.actualizarDesdeGaleria(
                uriSeleccionada
            )
        }
        // Si cancela la selección, no cambia la foto.
    }

    // CÁMARA: capturar una nueva fotografía
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
        onModoEstudioClick = onModoEstudioClick,
        onVolverClick = onVolverClick
    )
}

@Composable
private fun ContenidoPerfil(
    nombre: String,
    curso: String,
    progreso: Float,
    imagenUri: Uri?,
    onAbrirGaleria: () -> Unit,
    onAbrirCamara: () -> Unit,
    errorCamara: String?,
    onContinuarClick: () -> Unit,
    onModoEstudioClick: () -> Unit,
    onVolverClick: () -> Unit
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
                    text = "Aprender y mejorar cada día",
                    fontSize = 15.sp,
                    color = MPAZTextoSecundario
                )
            }

            MPAZCard(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Mi progreso de aprendizaje",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MPAZTexto
                )

                Spacer(Modifier.height(16.dp))

                MPAZProgress(
                    progreso = progresoSeguro,
                    etiqueta = "Mi progreso"
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Progreso de ejemplo. " +
                            "Pronto podrás ver tus avances reales.",
                    fontSize = 13.sp,
                    color = MPAZTextoSecundario
                )
            }

            MPAZButton(
                texto = "Continuar a asignaturas →",
                onClick = onContinuarClick,
                modifier = Modifier.widthIn(max = 480.dp)
            )

            MPAZSecondaryButton(
                texto = "Ir a modo estudio",
                onClick = onModoEstudioClick,
                modifier = Modifier.widthIn(max = 480.dp)
            )

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Preview(
    name = "MPAZ - Perfil cámara y galería",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PerfilScreenPreview() {

    AppMPAZPRO3_Grupo6Theme {

        ContenidoPerfil(
            nombre = "Camila González",
            curso = "3° Básico",
            progreso = 0.6f,
            imagenUri = null,
            onAbrirGaleria = {},
            onAbrirCamara = {},
            errorCamara = null,
            onContinuarClick = {},
            onModoEstudioClick = {},
            onVolverClick = {}
        )
    }
}