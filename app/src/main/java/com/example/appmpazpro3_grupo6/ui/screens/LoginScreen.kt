package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmpazpro3_grupo6.ui.components.*
import com.example.appmpazpro3_grupo6.ui.theme.*
import com.example.appmpazpro3_grupo6.validation.ValidadorFormulario
import com.example.appmpazpro3_grupo6.viewmodel.CuentaViewModel

@Composable
fun LoginScreen(
    onVolverClick: () -> Unit = {},
    onCrearCuentaClick: () -> Unit = {},
    onIngresarClick: (String, String) -> Unit = { _, _ -> },
    cuentaViewModel: CuentaViewModel = viewModel()
) {

    val procesando by cuentaViewModel.procesando.collectAsState()
    val mensajeError by cuentaViewModel.mensajeError.collectAsState()

    FormularioLogin(
        procesando = procesando,
        mensajeError = mensajeError,
        onVolverClick = onVolverClick,
        onCrearCuentaClick = onCrearCuentaClick,
        onEditar = {
            cuentaViewModel.limpiarError()
        },
        onIniciarSesion = { correo, contrasena ->

            cuentaViewModel.iniciarSesion(
                correo = correo,
                contrasena = contrasena,
                onExito = { estudiante ->

                    onIngresarClick(
                        estudiante.nombre,
                        estudiante.curso
                    )
                }
            )
        }
    )
}

@Composable
private fun FormularioLogin(
    procesando: Boolean,
    mensajeError: String?,
    onVolverClick: () -> Unit,
    onCrearCuentaClick: () -> Unit,
    onEditar: () -> Unit,
    onIniciarSesion: (String, String) -> Unit
) {

    var correo by rememberSaveable {
        mutableStateOf("")
    }

    var contrasena by remember {
        mutableStateOf("")
    }

    var errorCorreo by remember {
        mutableStateOf<String?>(null)
    }

    var errorContrasena by remember {
        mutableStateOf<String?>(null)
    }

    MPAZBackground {

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {

            val alturaPantalla = maxHeight

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = alturaPantalla)
                        .padding(
                            horizontal = 24.dp,
                            vertical = 20.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Column(
                        modifier = Modifier
                            .widthIn(max = 480.dp)
                            .fillMaxWidth()
                    ) {

                        MPAZScreenHeader(
                            titulo = "Iniciar sesión",
                            subtitulo = "¡Qué bueno verte de nuevo!",
                            onVolverClick = onVolverClick
                        )

                        Spacer(Modifier.height(16.dp))

                        MPAZCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Text(
                                text = "¡Bienvenido!",
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold,
                                color = MPAZTexto
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = "Ingresa tus datos para continuar",
                                fontSize = 15.sp,
                                color = MPAZTextoSecundario
                            )

                            Spacer(Modifier.height(24.dp))

                            MPAZTextField(
                                valor = correo,
                                onValorChange = {
                                    correo = it
                                    errorCorreo = null
                                    onEditar()
                                },
                                etiqueta = "Correo electrónico",
                                placeholder = "ejemplo@correo.com",
                                tipoTeclado = KeyboardType.Email,
                                mensajeError = errorCorreo
                            )

                            Spacer(Modifier.height(16.dp))

                            MPAZTextField(
                                valor = contrasena,
                                onValorChange = {
                                    contrasena = it
                                    errorContrasena = null
                                    onEditar()
                                },
                                etiqueta = "Contraseña",
                                esContrasena = true,
                                mensajeError = errorContrasena
                            )

                            Spacer(Modifier.height(24.dp))

                            if (mensajeError != null) {

                                Text(
                                    text = mensajeError,
                                    fontSize = 14.sp,
                                    color = MPAZCoral,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(Modifier.height(14.dp))
                            }
                            if (procesando) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement =
                                        Arrangement.Center,
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    CircularProgressIndicator(
                                        modifier = Modifier.size(22.dp),
                                        color = MPAZCoral,
                                        strokeWidth = 2.dp
                                    )

                                    Spacer(Modifier.width(12.dp))

                                    Text(
                                        text = "Verificando cuenta...",
                                        color = MPAZTextoSecundario
                                    )
                                }

                                Spacer(Modifier.height(16.dp))
                            }

                            MPAZButton(
                                texto = if (procesando) {
                                    "Ingresando..."
                                } else {
                                    "Ingresar →"
                                },
                                onClick = {

                                    if (!procesando) {

                                        val resultado =
                                            ValidadorFormulario.validarLogin(
                                                correo = correo,
                                                contrasena = contrasena
                                            )

                                        errorCorreo = resultado.correo

                                        errorContrasena =
                                            resultado.contrasena

                                        if (resultado.esValido) {

                                            onIniciarSesion(
                                                correo.trim(),
                                                contrasena
                                            )
                                        }
                                    }
                                }
                            )

                            Spacer(Modifier.height(12.dp))

                            MPAZSecondaryButton(
                                texto = "Crear cuenta",
                                onClick = onCrearCuentaClick
                            )

                            Spacer(Modifier.height(16.dp))

                            Text(
                                text = "Inicio de sesión con cuenta local",
                                fontSize = 13.sp,
                                color = MPAZTextoSecundario,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    name = "MPAZ - Login con cuenta local",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PreviewLoginScreen() {

    AppMPAZPRO3_Grupo6Theme {

        FormularioLogin(
            procesando = false,
            mensajeError = null,
            onVolverClick = {},
            onCrearCuentaClick = {},
            onEditar = {},
            onIniciarSesion = { _, _ -> }
        )
    }
}