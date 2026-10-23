package com.example.appmpazpro3_grupo6.ui.screens

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
import com.example.appmpazpro3_grupo6.ui.components.*
import com.example.appmpazpro3_grupo6.ui.theme.*
import com.example.appmpazpro3_grupo6.validation.ValidadorFormulario

@Composable
fun LoginScreen(
    onVolverClick: () -> Unit = {},
    onCrearCuentaClick: () -> Unit = {},
    onIngresarClick: () -> Unit = {}
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
                                },
                                etiqueta = "Contraseña",
                                esContrasena = true,
                                mensajeError = errorContrasena
                            )

                            Spacer(Modifier.height(24.dp))

                            MPAZButton(
                                texto = "Ingresar →",
                                onClick = {

                                    val resultado =
                                        ValidadorFormulario.validarLogin(
                                            correo = correo,
                                            contrasena = contrasena
                                        )

                                    errorCorreo = resultado.correo
                                    errorContrasena =
                                        resultado.contrasena

                                    if (resultado.esValido) {
                                        onIngresarClick()
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
                                text = "Inicio de sesión en modo de prueba",
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
    name = "MPAZ - Login con validación centralizada",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PreviewLoginScreen() {

    AppMPAZPRO3_Grupo6Theme {
        LoginScreen()
    }
}