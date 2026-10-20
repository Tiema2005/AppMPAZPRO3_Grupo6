package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.layout.*
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
    onContinuarClick: () -> Unit = {},
    onModoEstudioClick: () -> Unit = {},
    onVolverClick: () -> Unit = {}
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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

            Text("Objetivo: Aprender y mejorar cada día")
            Spacer(Modifier.height(24.dp))

            LinearProgressIndicator(
                progress = { 0.6f },
                modifier = Modifier.fillMaxWidth()
            )
            Text("Mi progreso: 60%")
            Spacer(Modifier.height(32.dp))

            Button(onClick = onContinuarClick) {
                Text("Continuar a asignaturas")
            }
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onModoEstudioClick
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