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
fun RegistroScreen(
    onVolverClick: () -> Unit = {},
    onRegistroExitoso: (String, String) -> Unit = { _, _ -> },
    cuentaViewModel: CuentaViewModel = viewModel()
) {
    val procesando by cuentaViewModel.procesando.collectAsState()
    val mensajeError by cuentaViewModel.mensajeError.collectAsState()

    FormularioRegistro(
        procesando = procesando,
        mensajeError = mensajeError,
        onVolverClick = onVolverClick,
        onEditar = {
            cuentaViewModel.limpiarError()
        },
        onRegistrar = { nombre, correo, contrasena, curso ->

            cuentaViewModel.registrar(
                nombre = nombre,
                correo = correo,
                contrasena = contrasena,
                curso = curso,
                onExito = { estudiante ->

                    onRegistroExitoso(
                        estudiante.nombre,
                        estudiante.curso
                    )
                }
            )
        }
    )
}

@Composable
private fun FormularioRegistro(
    procesando: Boolean,
    mensajeError: String?,
    onVolverClick: () -> Unit,
    onEditar: () -> Unit,
    onRegistrar: (String, String, String, String) -> Unit
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

            MPAZScreenHeader(
                titulo = "Crear cuenta",
                subtitulo = "¡Comienza tu aventura de aprendizaje!",
                onVolverClick = onVolverClick
            )

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

                MPAZTextField(
                    valor = nombre,
                    onValorChange = {
                        nombre = it
                        errorNombre = null
                        onEditar()
                    },
                    etiqueta = "Nombre completo",
                    placeholder = "Escribe tu nombre",
                    mensajeError = errorNombre
                )

                Spacer(Modifier.height(14.dp))

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

                Spacer(Modifier.height(14.dp))

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

                Spacer(Modifier.height(14.dp))

                MPAZTextField(
                    valor = confirmarContrasena,
                    onValorChange = {
                        confirmarContrasena = it
                        errorConfirmacion = null
                        onEditar()
                    },
                    etiqueta = "Confirmar contraseña",
                    esContrasena = true,
                    mensajeError = errorConfirmacion
                )

                Spacer(Modifier.height(14.dp))

                MPAZTextField(
                    valor = curso,
                    onValorChange = {},
                    etiqueta = "Curso",
                    readOnly = true
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

                    Spacer(Modifier.height(12.dp))
                }

                if (procesando) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = MPAZCoral,
                            strokeWidth = 2.dp
                        )

                        Spacer(Modifier.width(12.dp))

                        Text(
                            text = "Guardando cuenta...",
                            color = MPAZTextoSecundario
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                }

                MPAZButton(
                    texto = if (procesando) {
                        "Guardando..."
                    } else {
                        "Crear mi cuenta"
                    },
                    onClick = {

                        if (!procesando) {

                            val resultado =
                                ValidadorFormulario.validarRegistro(
                                    nombre = nombre,
                                    correo = correo,
                                    contrasena = contrasena,
                                    confirmarContrasena =
                                        confirmarContrasena
                                )

                            errorNombre = resultado.nombre
                            errorCorreo = resultado.correo
                            errorContrasena =
                                resultado.contrasena
                            errorConfirmacion =
                                resultado.confirmacion

                            if (resultado.esValido) {

                                onRegistrar(
                                    nombre.trim(),
                                    correo.trim(),
                                    contrasena,
                                    curso
                                )
                            }
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Cuenta guardada en este dispositivo",
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
    name = "MPAZ - Registro con almacenamiento local",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PreviewRegistroScreen() {

    AppMPAZPRO3_Grupo6Theme {

        FormularioRegistro(
            procesando = false,
            mensajeError = null,
            onVolverClick = {},
            onEditar = {},
            onRegistrar = { _, _, _, _ -> }
        )
    }
}