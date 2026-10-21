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
fun AsignaturasScreen(
    onVolverClick: () -> Unit = {},
    onAsignaturaClick: (String) -> Unit = {}
) {
    val asignaturas = listOf(
        "📖 Lenguaje y Comunicación",
        "🔢 Matemática",
        "🧪 Ciencias Naturales",
        "🌎 Historia, Geografía y Ciencias Sociales"
    )

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Asignaturas",
                fontSize = 30.sp
            )

            Text("Selecciona una asignatura")

            Spacer(Modifier.height(12.dp))

            asignaturas.forEach { asignatura ->
                ElevatedCard(
                    onClick = {
                        onAsignaturaClick(asignatura)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = asignatura,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onVolverClick,
                modifier = Modifier.fillMaxWidth()
            ) {
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