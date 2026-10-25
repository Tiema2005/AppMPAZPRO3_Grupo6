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
import com.example.appmpazpro3_grupo6.ui.components.*
import com.example.appmpazpro3_grupo6.ui.theme.*
import com.example.appmpazpro3_grupo6.viewmodel.EstadoViewModel

@Composable
fun ModoEstudioScreen(
    onVolverClick: () -> Unit = {},
    estadoViewModel: EstadoViewModel = viewModel()
) {
    val activo by estadoViewModel.activo.collectAsState()
    val mostrarMensaje by estadoViewModel.mostrarMensaje.collectAsState()
    val guardando by estadoViewModel.guardando.collectAsState()

    ContenidoModoEstudio(
        activo = activo,
        mostrarMensaje = mostrarMensaje,
        guardando = guardando,
        onAlternar = { estadoViewModel.alternarEstado() },
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

    val colorAnimado by animateColorAsState(
        targetValue = if (activo == true) {
            Color(0xFF008F55)
        } else {
            MPAZCoral
        },
        animationSpec = tween(durationMillis = 500),
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
                subtitulo = "¡Aprende con ayuda paso a paso!",
                onVolverClick = onVolverClick
            )

            MascotaMPAZ(
                modifier = Modifier.size(145.dp)
            )

            MPAZLogo(compacto = true)

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

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Activa las pistas y consejos que te " +
                            "ayudarán a resolver tus ejercicios.",
                    fontSize = 15.sp,
                    color = MPAZTextoSecundario,
                    lineHeight = 22.sp
                )

                Spacer(Modifier.height(24.dp))

                if (activo == null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = MPAZCoral)

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "Cargando preferencias...",
                            color = MPAZTextoSecundario
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

                        Column {
                            Text(
                                text = "Estado actual",
                                fontSize = 14.sp,
                                color = MPAZTextoSecundario
                            )

                            Spacer(Modifier.height(4.dp))

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

                    Spacer(Modifier.height(26.dp))

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
                            text = if (activo) {
                                "Desactivar modo estudio"
                            } else {
                                "Activar modo estudio"
                            },
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    if (guardando) {
                        Spacer(Modifier.height(14.dp))

                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = MPAZAzul,
                            trackColor = MPAZAzulSuave
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Guardando preferencia...",
                            fontSize = 14.sp,
                            color = MPAZTextoSecundario
                        )
                    }

                    AnimatedVisibility(visible = mostrarMensaje) {
                        Column {
                            Spacer(Modifier.height(16.dp))

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
            }

            MPAZCard(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth()
            ) {
                TextButton(
                    onClick = { mostrarAyuda = !mostrarAyuda },
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

                AnimatedVisibility(visible = mostrarAyuda) {
                    Column {
                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "Con el modo estudio activado, " +
                                    "en cada ejercicio podrás pedir una " +
                                    "pista antes de responder. Después " +
                                    "verás la explicación y un consejo " +
                                    "para aprender mejor.",
                            fontSize = 15.sp,
                            color = MPAZTextoSecundario,
                            lineHeight = 23.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "Tus porcentajes de avance se " +
                                    "guardan igual en ambos modos.",
                            fontSize = 14.sp,
                            color = MPAZTexto,
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

            Spacer(Modifier.height(12.dp))
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