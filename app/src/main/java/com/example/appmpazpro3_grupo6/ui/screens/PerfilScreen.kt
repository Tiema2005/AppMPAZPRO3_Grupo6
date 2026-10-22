package com.example.appmpazpro3_grupo6.ui.screens

import android.graphics.ImageDecoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.appmpazpro3_grupo6.ui.components.*
import com.example.appmpazpro3_grupo6.ui.theme.*
import java.io.File

@Composable
fun PerfilScreen(
    nombre: String = "Estudiante de prueba",
    curso: String = "3° Básico",
    progreso: Float = 0.6f,
    onContinuarClick: () -> Unit = {},
    onModoEstudioClick: () -> Unit = {},
    onVolverClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val carpetaPerfil = remember(context) {
        File(context.filesDir, "perfil").apply {
            mkdirs()
        }
    }

    val archivoFoto = remember(carpetaPerfil) {
        File(carpetaPerfil, "foto_perfil.jpg")
    }

    val archivoTemporal = remember(carpetaPerfil) {
        File(carpetaPerfil, "captura_temporal.jpg")
    }

    var versionFoto by remember {
        mutableIntStateOf(0)
    }

    val fotoPerfil = remember(versionFoto, archivoFoto) {
        if (archivoFoto.exists() && archivoFoto.length() > 0L) {
            runCatching {
                val source = ImageDecoder.createSource(archivoFoto)

                ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
                    val dimensionMayor = maxOf(
                        info.size.width,
                        info.size.height
                    )

                    val muestra = (dimensionMayor / 640)
                        .coerceAtLeast(1)

                    decoder.setTargetSampleSize(muestra)
                }.asImageBitmap()
            }.getOrNull()
        } else {
            null
        }
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { fotoTomada ->

        if (
            fotoTomada &&
            archivoTemporal.exists() &&
            archivoTemporal.length() > 0L
        ) {
            val guardada = runCatching {
                archivoTemporal.copyTo(
                    target = archivoFoto,
                    overwrite = true
                )
            }.isSuccess

            if (guardada) {
                versionFoto++
            }
        }

        archivoTemporal.delete()
    }

    ContenidoPerfil(
        nombre = nombre,
        curso = curso,
        progreso = progreso,
        fotoPerfil = fotoPerfil,
        onTomarFoto = {
            val uriFoto = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                archivoTemporal
            )

            camaraLauncher.launch(uriFoto)
        },
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
    fotoPerfil: ImageBitmap?,
    onTomarFoto: () -> Unit,
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(MPAZAzulSuave),
                        contentAlignment = Alignment.Center
                    ) {
                        if (fotoPerfil != null) {
                            Image(
                                bitmap = fotoPerfil,
                                contentDescription = "Foto del estudiante",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = "👤",
                                fontSize = 52.sp
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {

                        Text(
                            text = nombre,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold,
                            color = MPAZTexto
                        )

                        Text(
                            text = curso,
                            fontSize = 16.sp,
                            color = MPAZTextoSecundario
                        )

                        Text(
                            text = "Estudiante MPAZ",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MPAZAzul
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                MPAZButton(
                    texto = "📷 Tomar foto",
                    onClick = onTomarFoto
                )

                Spacer(Modifier.height(20.dp))

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

            Spacer(Modifier.height(4.dp))

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
    name = "MPAZ - Perfil con fotografía",
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
            fotoPerfil = null,
            onTomarFoto = {},
            onContinuarClick = {},
            onModoEstudioClick = {},
            onVolverClick = {}
        )
    }
}