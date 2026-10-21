package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmpazpro3_grupo6.viewmodel.EstadoViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

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

    val textoBoton by remember(activo) {
        derivedStateOf {
            if (activo == true) {
                "Desactivar modo estudio"
            } else {
                "Activar modo estudio"
            }
        }
    }

    val colorAnimado by animateColorAsState(
        targetValue = if (activo == true) {
            Color(0xFF008F55)
        } else {
            Color(0xFF0066CC)
        },
        animationSpec = tween(durationMillis = 500),
        label = "ColorModoEstudio"
    )

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "MPAZ PRO 3.º",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0066CC)
            )
            Spacer(Modifier.height(24.dp))

            Text(
                text = "Modo estudio",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))

            if (activo == null) {
                CircularProgressIndicator()
                Spacer(Modifier.height(16.dp))
                Text("Cargando preferencias...")
            } else {
                Text(
                    text = if (activo) {
                        "Modo estudio activado"
                    } else {
                        "Modo estudio desactivado"
                    },
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = onAlternar,
                    enabled = !guardando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorAnimado
                    )
                ) {
                    Text(
                        text = textoBoton,
                        fontSize = 18.sp
                    )
                }
                if (guardando) {
                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(Modifier.height(20.dp))

                AnimatedVisibility(
                    visible = mostrarMensaje
                ) {
                    Text(
                        text = "¡Estado guardado correctamente!",
                        color = Color(0xFF008F55),
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(16.dp))

                TextButton(
                    onClick = {
                        mostrarAyuda = !mostrarAyuda
                    }
                ) {
                    Text(
                        if (mostrarAyuda)
                            "Ocultar información"
                        else
                            "¿Qué es el modo estudio?"
                    )
                }

                AnimatedVisibility(
                    visible = mostrarAyuda
                ) {
                    Text(
                        text = "Esta opción guarda tu preferencia " +
                                "de estudio en el dispositivo.",
                        fontSize = 15.sp
                    )
                }
            }
            Spacer(Modifier.height(24.dp))

            OutlinedButton(
                onClick = onVolverClick
            ) {
                Text("Volver al perfil")
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun ModoEstudioScreenPreview() {
    MaterialTheme {
        ContenidoModoEstudio(
            activo = false,
            mostrarMensaje = false,
            guardando = false,
            onAlternar = {},
            onVolverClick = {}
        )
    }
}