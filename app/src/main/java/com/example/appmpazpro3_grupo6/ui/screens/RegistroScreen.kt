package com.example.appmpazpro3_grupo6.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onVolverClick: () -> Unit = {},
    onRegistroExitoso: (String, String) -> Unit = { _, _ -> }
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }
    var curso by rememberSaveable { mutableStateOf("3° Básico") }

    var errorNombre by rememberSaveable { mutableStateOf("") }
    var errorCorreo by rememberSaveable { mutableStateOf("") }
    var errorContrasena by rememberSaveable { mutableStateOf("") }
    var errorConfirmar by rememberSaveable { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Crear cuenta") }
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "MPAZ PRO 3.º",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0066CC)
            )

            Text(
                text = "Crea tu cuenta",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    errorNombre = ""
                },
                label = { Text("Nombre completo") },
                isError = errorNombre.isNotEmpty(),
                supportingText = {
                    if (errorNombre.isNotEmpty()) Text(errorNombre)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    errorCorreo = ""
                },
                label = { Text("Correo electrónico") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                isError = errorCorreo.isNotEmpty(),
                supportingText = {
                    if (errorCorreo.isNotEmpty()) Text(errorCorreo)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    errorContrasena = ""
                },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                isError = errorContrasena.isNotEmpty(),
                supportingText = {
                    if (errorContrasena.isNotEmpty()) Text(errorContrasena)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmar,
                onValueChange = {
                    confirmar = it
                    errorConfirmar = ""
                },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                isError = errorConfirmar.isNotEmpty(),
                supportingText = {
                    if (errorConfirmar.isNotEmpty()) Text(errorConfirmar)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = curso,
                onValueChange = { },
                label = { Text("Curso") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    errorNombre = if (nombre.isBlank())
                        "Ingresa tu nombre" else ""
                    errorCorreo = when {
                        correo.isBlank() ->
                            "Ingresa tu correo"
                        !Patterns.EMAIL_ADDRESS.matcher(
                            correo.trim()
                        ).matches() ->
                            "Correo inválido"
                        else -> ""
                    }
                    errorContrasena = when {
                        contrasena.isBlank() ->
                            "Ingresa una contraseña"
                        contrasena.length < 6 ->
                            "Mínimo 6 caracteres"
                        else -> ""
                    }
                    errorConfirmar = if (confirmar != contrasena)
                        "Las contraseñas no coinciden" else ""
                    if (
                        errorNombre.isEmpty() &&
                        errorCorreo.isEmpty() &&
                        errorContrasena.isEmpty() &&
                        errorConfirmar.isEmpty()
                    ) {
                        onRegistroExitoso(nombre.trim(), curso)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0066CC)
                )
            ) {
                Text("Crear cuenta", fontSize = 18.sp)
            }
            TextButton(onClick = onVolverClick) {
                Text("Volver al inicio de sesión")
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
fun RegistroScreenPreview() {
    MaterialTheme {
        RegistroScreen()
    }
}