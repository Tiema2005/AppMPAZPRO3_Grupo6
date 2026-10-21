package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    val porcentaje = (progresoSeguro * 100).toInt()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Mi Perfil", fontSize = 30.sp)

            Spacer(Modifier.height(24.dp))

            Text("👤", fontSize = 70.sp)

            Spacer(Modifier.height(16.dp))

            Text("Nombre: $nombre", fontSize = 20.sp)
            Text("Curso: $curso", fontSize = 18.sp)

            Spacer(Modifier.height(12.dp))

            Text("Objetivo: Aprender y mejorar cada día")

            Spacer(Modifier.height(24.dp))

            LinearProgressIndicator(
                progress = { progresoSeguro },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Text("Mi progreso: $porcentaje%")

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onContinuarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Continuar a asignaturas")
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onModoEstudioClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Activar modo estudio")
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = onVolverClick) {
                Text("Volver")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilScreenPreview() {
    MaterialTheme {
        PerfilScreen(
            nombre = "Camila González",
            curso = "3° Básico"
        )
    }
}
