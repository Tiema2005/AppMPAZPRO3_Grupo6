
package com.example.appmpazpro3_grupo6.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.appmpazpro3_grupo6.ui.components.MPAZBackground
import com.example.appmpazpro3_grupo6.ui.components.MPAZButton
import com.example.appmpazpro3_grupo6.ui.components.MPAZCard
import com.example.appmpazpro3_grupo6.ui.components.MPAZScreenHeader
import com.example.appmpazpro3_grupo6.ui.components.MPAZTextField

import com.example.appmpazpro3_grupo6.ui.theme.*

@Composable
fun RegistroScreen(
    onVolverClick: () -> Unit = {},
    onRegistroExitoso: (String, String) -> Unit = { _, _ -> }
) {

    var nombre by rememberSaveable {
        mutableStateOf("")
    }

    var correo by rememberSaveable {
        mutableStateOf("")
    }

    var contrasena by remember {
        mutableStateOf("")
    }

    var confirmarContrasena by remember {
        mutableStateOf("")
    }

    val curso = "3° Básico"

    var errorNombre by remember {
        mutableStateOf<String?>(null)
    }

    var errorCorreo by remember {
        mutableStateOf<String?>(null)
    }

    var errorContrasena by remember {
        mutableStateOf<String?>(null)
    }

    var errorConfirmacion by remember {
        mutableStateOf<String?>(null)
    }

    MPAZBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 24.dp,
                    vertical = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Encabezado de registro
            MPAZScreenHeader(
                titulo = "Crear cuenta",
                subtitulo = "¡Comienza tu aventura de aprendizaje!",
                onVolverClick = onVolverClick
            )

            // Tarjeta de datos justo debajo del encabezado
            MPAZCard(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Datos del estudiante",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = MPAZTexto
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Completa tus datos para continuar",
                    fontSize = 15.sp,
                    color = MPAZTextoSecundario
                )

                Spacer(Modifier.height(22.dp))

                // Nombre completo
                MPAZTextField(
                    valor = nombre,
                    onValorChange = {
                        nombre = it
                        errorNombre = null
                    },
                    etiqueta = "Nombre completo",
                    placeholder = "Escribe tu nombre",
                    mensajeError = errorNombre
                )

                Spacer(Modifier.height(14.dp))

                // Correo electrónico
                MPAZTextField(
                    valor = correo,
                    onValorChange = {
                        correo = it
                        errorCorreo = null
                    },
                    etiqueta = "Correo electrónico",
                    placeholder = "ejemplo@correo.com",
                    tipoTeclado = KeyboardType.Email,
                    mensajeError = errorCorreo
                )

                Spacer(Modifier.height(14.dp))

                // Contraseña
                MPAZTextField(
                    valor = contrasena,
                    onValorChange = {
                        contrasena = it
                        errorContrasena = null
                    },
                    etiqueta = "Contraseña",
                    esContrasena = true,
                    mensajeError = errorContrasena
                )

                Spacer(Modifier.height(14.dp))

                // Confirmación de contraseña
                MPAZTextField(
                    valor = confirmarContrasena,
                    onValorChange = {
                        confirmarContrasena = it
                        errorConfirmacion = null
                    },
                    etiqueta = "Confirmar contraseña",
                    esContrasena = true,
                    mensajeError = errorConfirmacion
                )

                Spacer(Modifier.height(14.dp))

                // Curso fijo del estudiante
                MPAZTextField(
                    valor = curso,
                    onValorChange = {},
                    etiqueta = "Curso",
                    readOnly = true
                )

                Spacer(Modifier.height(26.dp))

                // Botón de registro con validaciones
                MPAZButton(
                    texto = "Crear mi cuenta ",
                    onClick = {

                        val correoValido =
                            Patterns.EMAIL_ADDRESS
                                .matcher(correo.trim())
                                .matches()

                        errorNombre = if (nombre.isBlank()) {
                            "Ingresa tu nombre"
                        } else {
                            null
                        }

                        errorCorreo = when {
                            correo.isBlank() ->
                                "Ingresa tu correo"

                            !correoValido ->
                                "Ingresa un correo válido"

                            else -> null
                        }

                        errorContrasena = when {
                            contrasena.isBlank() ->
                                "Ingresa una contraseña"

                            contrasena.length < 6 ->
                                "Debe tener al menos 6 caracteres"

                            else -> null
                        }

                        errorConfirmacion = when {
                            confirmarContrasena.isBlank() ->
                                "Confirma tu contraseña"

                            confirmarContrasena != contrasena ->
                                "Las contraseñas no coinciden"

                            else -> null
                        }

                        // Continuar solamente si los datos son válidos
                        if (
                            errorNombre == null &&
                            errorCorreo == null &&
                            errorContrasena == null &&
                            errorConfirmacion == null
                        ) {
                            onRegistroExitoso(
                                nombre.trim(),
                                curso
                            )
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Registro en modo de prueba",
                    fontSize = 13.sp,
                    color = MPAZTextoSecundario,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(
    name = "MPAZ - Registro sin logo",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PreviewRegistroScreen() {
    AppMPAZPRO3_Grupo6Theme {
        RegistroScreen()
    }
}