package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmpazpro3_grupo6.ui.components.MPAZBackground
import com.example.appmpazpro3_grupo6.ui.components.MPAZCard
import com.example.appmpazpro3_grupo6.ui.components.MPAZLogo
import com.example.appmpazpro3_grupo6.ui.components.MPAZScreenHeader
import com.example.appmpazpro3_grupo6.ui.components.MPAZSecondaryButton
import com.example.appmpazpro3_grupo6.ui.components.MascotaMPAZ
import com.example.appmpazpro3_grupo6.ui.theme.*
import com.example.appmpazpro3_grupo6.viewmodel.EstadoViewModel

@Composable
fun ModoEstudioScreen(
    onVolverClick: () -> Unit = {},
    estadoViewModel: EstadoViewModel = viewModel()
) {

    val activo by estadoViewModel.activo.collectAsState()

    val mostrarMensaje by
    estadoViewModel.mostrarMensaje.collectAsState()

    val guardando by
    estadoViewModel.guardando.collectAsState()

    ContenidoModoEstudio(
        activo = activo,
        mostrarMensaje = mostrarMensaje,
        guardando = guardando,
        onAlternar = {
            estadoViewModel.alternarEstado()
        },
        onVolverClick = onVolverClick
    )
}

@Composable
private fun ContenidoModoEstudio(
    activo: Boolean?,
    mostrarMensaje: Boolean,
    guardando: Boolean,
    onAlternar: () -> Unit,
    onVolverClick: () -> Unit
) {

    var mostrarAyuda by rememberSaveable {
        mutableStateOf(false)
    }

    val textoBoton = if (activo == true) {
        "Desactivar modo estudio"
    } else {
        "Activar modo estudio"
    }

    val colorAnimado by animateColorAsState(
        targetValue = if (activo == true) {
            Color(0xFF008F55)
        } else {
            MPAZCoral
        },
        animationSpec = tween(
            durationMillis = 500
        ),
        label = "ColorModoEstudio"
    )

    MPAZBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 24.dp,
                    vertical = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            MPAZScreenHeader(
                titulo = "Modo estudio",
                subtitulo = "¡Prepárate para aprender!",
                onVolverClick = onVolverClick
            )

            MascotaMPAZ(
                modifier = Modifier.size(145.dp)
            )

            MPAZLogo(
                compacto = true
            )

            MPAZCard(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Mi modo estudio",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = MPAZTexto
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = "Organiza tu aprendizaje y " +
                            "guarda tu preferencia.",
                    fontSize = 15.sp,
                    color = MPAZTextoSecundario,
                    lineHeight = 22.sp
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                if (activo == null) {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        CircularProgressIndicator(
                            color = MPAZCoral
                        )

                        Text(
                            text = "Cargando preferencias...",
                            color = MPAZTextoSecundario,
                            fontSize = 15.sp
                        )
                    }

                } else {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = if (activo) "✅" else "📚",
                            fontSize = 30.sp
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {

                            Text(
                                text = "Estado actual",
                                fontSize = 14.sp,
                                color = MPAZTextoSecundario
                            )

                            Text(
                                text = if (activo) {
                                    "Modo estudio activado"
                                } else {
                                    "Modo estudio desactivado"
                                },
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (activo) {
                                    Color(0xFF008F55)
                                } else {
                                    MPAZCoral
                                }
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(26.dp)
                    )

                    Button(
                        onClick = onAlternar,
                        enabled = !guardando,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorAnimado,
                            contentColor = MPAZBlanco
                        )
                    ) {

                        Text(
                            text = textoBoton,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    if (guardando) {

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = MPAZAzul,
                            trackColor = MPAZAzulSuave
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "Guardando preferencia...",
                            fontSize = 14.sp,
                            color = MPAZTextoSecundario
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    AnimatedVisibility(
                        visible = mostrarMensaje
                    ) {

                        MPAZCard(
                            modifier = Modifier.fillMaxWidth(),
                            fondo = Color(0xFFE9F8EF)
                        ) {

                            Text(
                                text = "✅ ¡Estado guardado correctamente!",
                                color = Color(0xFF008F55),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            MPAZCard(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth()
            ) {

                TextButton(
                    onClick = {
                        mostrarAyuda = !mostrarAyuda
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MPAZCoral
                    )
                ) {

                    Text(
                        text = if (mostrarAyuda) {
                            "Ocultar información ▲"
                        } else {
                            "¿Qué es el modo estudio? ▼"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                AnimatedVisibility(
                    visible = mostrarAyuda
                ) {

                    Column {

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = "Esta opción guarda tu " +
                                    "preferencia de estudio " +
                                    "en el dispositivo.",
                            fontSize = 15.sp,
                            color = MPAZTextoSecundario,
                            lineHeight = 23.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            MPAZSecondaryButton(
                texto = "Volver al perfil",
                onClick = onVolverClick,
                modifier = Modifier.widthIn(max = 480.dp)
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }
    }
}

@Preview(
    name = "MPAZ - Modo estudio desactivado",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun ModoEstudioScreenPreview() {
    AppMPAZPRO3_Grupo6Theme {
        ContenidoModoEstudio(
            activo = false,
            mostrarMensaje = false,
            guardando = false,
            onAlternar = {},
            onVolverClick = {}
        )
    }
}

@Preview(
    name = "MPAZ - Modo estudio activado",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun ModoEstudioActivadoPreview() {
    AppMPAZPRO3_Grupo6Theme {
        ContenidoModoEstudio(
            activo = true,
            mostrarMensaje = true,
            guardando = false,
            onAlternar = {},
            onVolverClick = {}
        )
    }
}