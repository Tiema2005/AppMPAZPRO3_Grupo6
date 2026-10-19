package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AsignaturasScreen(
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
            Text("Asignaturas", fontSize = 30.sp)
            Spacer(Modifier.height(12.dp))
            Text("Selecciona una asignatura")
            Spacer(Modifier.height(28.dp))
            val asignaturas = listOf(
                "📖 Lenguaje y Comunicación",
                "🔢 Matemática",
                "🧪 Ciencias Naturales",
                "🌎 Historia, Geografía y Ciencias Sociales"
            )
            asignaturas.forEach { asignatura ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Text(
                        text = asignatura,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            Button(onClick = onVolverClick) {
                Text("Volver al perfil")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AsignaturasScreenPreview() {
    MaterialTheme {
        AsignaturasScreen()
    }
}