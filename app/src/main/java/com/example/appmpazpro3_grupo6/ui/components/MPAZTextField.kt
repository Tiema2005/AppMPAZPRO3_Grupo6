package com.example.appmpazpro3_grupo6.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appmpazpro3_grupo6.ui.theme.MPAZBlanco
import com.example.appmpazpro3_grupo6.ui.theme.MPAZBorde
import com.example.appmpazpro3_grupo6.ui.theme.MPAZCoral
import com.example.appmpazpro3_grupo6.ui.theme.MPAZError
import com.example.appmpazpro3_grupo6.ui.theme.MPAZTextoSecundario

@Composable
fun MPAZTextField(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    esContrasena: Boolean = false,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    mensajeError: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    var contrasenaVisible by rememberSaveable {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = {
            Text(etiqueta)
        },
        placeholder = if (placeholder.isNotEmpty()) {
            {
                Text(placeholder)
            }
        } else {
            null
        },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        enabled = enabled,
        readOnly = readOnly,
        isError = !mensajeError.isNullOrBlank(),
        visualTransformation = if (
            esContrasena && !contrasenaVisible
        ) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (esContrasena) {
                KeyboardType.Password
            } else {
                tipoTeclado
            }
        ),
        trailingIcon = if (esContrasena) {
            {
                TextButton(
                    onClick = {
                        contrasenaVisible = !contrasenaVisible
                    }
                ) {
                    Text(
                        if (contrasenaVisible) "Ocultar" else "Ver"
                    )
                }
            }
        } else {
            null
        },
        supportingText = if (!mensajeError.isNullOrBlank()) {
            {
                Text(mensajeError)
            }
        } else {
            null
        },
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MPAZCoral,
            unfocusedBorderColor = MPAZBorde,
            focusedLabelColor = MPAZCoral,
            unfocusedLabelColor = MPAZTextoSecundario,
            focusedContainerColor = MPAZBlanco,
            unfocusedContainerColor = MPAZBlanco,
            errorBorderColor = MPAZError,
            errorLabelColor = MPAZError
        )
    )
}