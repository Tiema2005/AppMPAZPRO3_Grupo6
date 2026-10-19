
package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onVolverClick: () -> Unit = {},
    onCrearCuentaClick: () -> Unit = {},
    onIngresarClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Inicio de sesión")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "MPAZ PRO 3.º",
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Pantalla de inicio de sesión",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onIngresarClick
            ) {
                Text("Ingresar (modo prueba)")
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onCrearCuentaClick
            ) {
                Text("Crear cuenta")
            }

            Spacer(modifier = Modifier.height(16.dp))
            TextButton(
                onClick = onVolverClick
            ) {
                Text("Volver a bienvenida")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen()
    }
}