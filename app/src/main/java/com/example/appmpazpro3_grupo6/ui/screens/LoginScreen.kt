package com.example.appmpazpro3_grupo6.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    var correo by rememberSaveable {
        mutableStateOf("")
    }

    var contrasena by rememberSaveable {
        mutableStateOf("")
    }

    var mostrarContrasena by rememberSaveable {
        mutableStateOf(false)
    }

    var errorCorreo by rememberSaveable {
        mutableStateOf("")
    }

    var errorContrasena by rememberSaveable {
        mutableStateOf("")
    }

    Scaffold(
        containerColor = Color.White,
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
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Iniciar sesión",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    errorCorreo = ""
                },
                label = {
                    Text("Correo electrónico")
                },
                placeholder = {
                    Text("estudiante@correo.cl")
                },
                isError = errorCorreo.isNotEmpty(),
                supportingText = {
                    if (errorCorreo.isNotEmpty()) {
                        Text(errorCorreo)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    errorContrasena = ""
                },
                label = {
                    Text("Contraseña")
                },
                visualTransformation =
                    if (mostrarContrasena) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                trailingIcon = {
                    TextButton(
                        onClick = {
                            mostrarContrasena = !mostrarContrasena
                        }
                    ) {
                        Text(
                            if (mostrarContrasena)
                                "Ocultar"
                            else
                                "Ver"
                        )
                    }
                },
                isError = errorContrasena.isNotEmpty(),
                supportingText = {
                    if (errorContrasena.isNotEmpty()) {
                        Text(errorContrasena)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    errorCorreo = when {
                        correo.isBlank() ->
                            "Ingresa tu correo"
                        !Patterns.EMAIL_ADDRESS.matcher(
                            correo.trim()
                        ).matches() ->
                            "Correo electrónico inválido"
                        else -> ""
                    }
                    errorContrasena = when {
                        contrasena.isBlank() ->
                            "Ingresa tu contraseña"
                        contrasena.length < 6 ->
                            "Debe tener al menos 6 caracteres"
                        else -> ""
                    }
                    if (
                        errorCorreo.isEmpty() &&
                        errorContrasena.isEmpty()
                    ) {
                        onIngresarClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0066CC)
                )
            ) {
                Text(
                    "Ingresar (modo prueba)",
                    fontSize = 18.sp
                )
            }
            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = onCrearCuentaClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear cuenta")
            }
            Spacer(Modifier.height(12.dp))

            TextButton(
                onClick = onVolverClick
            ) {
                Text("Volver a bienvenida")
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
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen()
    }
}