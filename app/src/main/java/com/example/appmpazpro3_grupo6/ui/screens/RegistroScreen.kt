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
fun RegistroScreen(
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
            Text(
                text = "Crear cuenta",
                fontSize = 30.sp
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Regístrate en MPAZ PRO 3.º",
                fontSize = 18.sp
            )
            Spacer(Modifier.height(32.dp))
            Button(onClick = onVolverClick) {
                Text("Volver al inicio de sesión")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    MaterialTheme {
        RegistroScreen()
    }
}