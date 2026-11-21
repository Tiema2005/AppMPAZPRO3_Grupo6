package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmpazpro3_grupo6.repository.ProgresoEstudiante
import com.example.appmpazpro3_grupo6.ui.components.MPAZBackground
import com.example.appmpazpro3_grupo6.ui.components.MPAZCard
import com.example.appmpazpro3_grupo6.ui.components.MPAZScreenHeader
import com.example.appmpazpro3_grupo6.ui.theme.*

@Composable
fun SeleccionDificultadScreen(
    asignaturaId: String,
    progreso: ProgresoEstudiante,
    onSeleccionar: (DificultadMPAZ) -> Unit,
    onVolverClick: () -> Unit
) {

    val datos = datosAsignaturaMPAZ(asignaturaId)

    MPAZBackground {

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {

            val alturaPantalla = maxHeight

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = alturaPantalla)
                        .padding(
                            horizontal = 20.dp,
                            vertical = 24.dp
                        ),

                    horizontalAlignment = Alignment.CenterHorizontally,

                    verticalArrangement = Arrangement.Center
                ) {

                    Column(
                        modifier = Modifier
                            .widthIn(max = 500.dp)
                            .fillMaxWidth(),

                        horizontalAlignment = Alignment.CenterHorizontally,

                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        MPAZScreenHeader(
                            titulo = datos?.titulo ?: "Asignatura",

                            subtitulo = "Elige qué dificultad quieres practicar",

                            onVolverClick = onVolverClick
                        )

                        Text(
                            text = datos?.emoji ?: "📚",
                            fontSize = 52.sp
                        )

                        Text(
                            text = "Cada nivel tiene 5 preguntas",

                            color = MPAZTextoSecundario,

                            textAlign = TextAlign.Center
                        )

                        DificultadMPAZ.entries.forEach { nivel ->

                            val clave = "${asignaturaId}_${nivel.id}"

                            val completadas = progreso
                                .completados(clave)
                                .coerceIn(0, 5)

                            val fondo = when (nivel) {

                                DificultadMPAZ.FACIL ->
                                    MPAZVerdeSuave

                                DificultadMPAZ.MEDIO ->
                                    MPAZAmarilloSuave

                                DificultadMPAZ.DIFICIL ->
                                    MPAZRosaSuave
                            }

                            MPAZCard(
                                modifier = Modifier
                                    .widthIn(max = 500.dp)
                                    .fillMaxWidth(),

                                fondo = fondo,

                                onClick = {
                                    onSeleccionar(nivel)
                                }
                            ) {

                                Column(
                                    verticalArrangement =
                                        Arrangement.spacedBy(10.dp)
                                ) {

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),

                                        verticalAlignment =
                                            Alignment.CenterVertically
                                    ) {

                                        Text(
                                            text = nivel.estrellas,
                                            fontSize = 24.sp
                                        )

                                        Spacer(
                                            modifier = Modifier.width(10.dp)
                                        )

                                        Text(
                                            text = nivel.titulo,

                                            fontSize = 23.sp,

                                            fontWeight = FontWeight.Bold,

                                            color = MPAZTexto
                                        )

                                        Spacer(
                                            modifier = Modifier.weight(1f)
                                        )

                                        Text(
                                            text = "$completadas/5",

                                            fontWeight = FontWeight.Bold,

                                            color = MPAZTexto
                                        )
                                    }

                                    LinearProgressIndicator(
                                        progress = {
                                            completadas / 5f
                                        },

                                        modifier = Modifier.fillMaxWidth(),

                                        color = MPAZCoral,

                                        trackColor = MPAZBlanco
                                    )

                                    Text(
                                        text = when {

                                            completadas == 5 ->
                                                "✅ Completado · Toca para ver resultados"

                                            completadas > 0 ->
                                                "▶ Continuar donde quedaste"

                                            else ->
                                                "▶ Comenzar las 5 preguntas"
                                        },

                                        color = MPAZTexto,

                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    name = "MPAZ - Seleccion de dificultad",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
private fun SeleccionDificultadPreview() {

    AppMPAZPRO3_Grupo6Theme {

        SeleccionDificultadScreen(
            asignaturaId = "matematica",
            progreso = ProgresoEstudiante(),
            onSeleccionar = {},
            onVolverClick = {}
        )
    }
}