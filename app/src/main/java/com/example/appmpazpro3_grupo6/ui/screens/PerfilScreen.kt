package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmpazpro3_grupo6.ui.components.MPAZBackground
import com.example.appmpazpro3_grupo6.ui.components.MPAZButton
import com.example.appmpazpro3_grupo6.ui.components.MPAZCard
import com.example.appmpazpro3_grupo6.ui.components.MPAZProgress
import com.example.appmpazpro3_grupo6.ui.components.MPAZScreenHeader
import com.example.appmpazpro3_grupo6.ui.components.MPAZSecondaryButton
import com.example.appmpazpro3_grupo6.ui.components.MascotaMPAZ
import com.example.appmpazpro3_grupo6.ui.theme.*

@Composable
fun PerfilScreen(
    nombre: String = "Estudiante de prueba",
    curso: String = "3° Básico",
    progreso: Float = 0.6f,
    onContinuarClick: () -> Unit = {},
    onModoEstudioClick: () -> Unit = {},
    onVolverClick: () -> Unit = {}
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
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    MascotaMPAZ(
                        modifier = Modifier.size(110.dp)
                    )

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

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "🎯 Mi objetivo",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MPAZTexto
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

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

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                MPAZProgress(
                    progreso = progresoSeguro,
                    etiqueta = "Mi progreso"
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Progreso de ejemplo. " +
                            "Pronto podrás ver tus avances reales.",
                    fontSize = 13.sp,
                    color = MPAZTextoSecundario
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

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

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }
    }
}

@Preview(
    name = "MPAZ - Mi perfil",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PerfilScreenPreview() {
    AppMPAZPRO3_Grupo6Theme {
        PerfilScreen(
            nombre = "Camila González",
            curso = "3° Básico",
            progreso = 0.6f
        )
    }
}