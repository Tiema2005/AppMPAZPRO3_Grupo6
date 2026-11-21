package com.example.appmpazpro3_grupo6.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Stable
class AjustesAccesibilidadMPAZ(context: Context) {

    private val almacenamiento = context.getSharedPreferences(
        "accesibilidad_mpaz",
        Context.MODE_PRIVATE
    )

    var textoGrande by mutableStateOf(
        almacenamiento.getBoolean(
            "texto_grande",
            false
        )
    )
        private set

    var altoContraste by mutableStateOf(
        almacenamiento.getBoolean(
            "alto_contraste",
            false
        )
    )
        private set

    var botonesGrandes by mutableStateOf(
        almacenamiento.getBoolean(
            "botones_grandes",
            false
        )
    )
        private set

    fun cambiarTextoGrande(valor: Boolean) {
        textoGrande = valor

        almacenamiento.edit()
            .putBoolean("texto_grande", valor)
            .apply()
    }

    fun cambiarContraste(valor: Boolean) {
        altoContraste = valor

        almacenamiento.edit()
            .putBoolean("alto_contraste", valor)
            .apply()
    }

    fun cambiarBotones(valor: Boolean) {
        botonesGrandes = valor

        almacenamiento.edit()
            .putBoolean("botones_grandes", valor)
            .apply()
    }
}

@Composable
fun recordarAjustesAccesibilidadMPAZ(): AjustesAccesibilidadMPAZ {
    val contexto = LocalContext.current.applicationContext

    return remember(contexto) {
        AjustesAccesibilidadMPAZ(contexto)
    }
}

@Composable
fun ConfiguracionAccesibilidadMPAZ(
    ajustes: AjustesAccesibilidadMPAZ,
    modifier: Modifier = Modifier
) {
    var abierto by rememberSaveable {
        mutableStateOf(false)
    }

    TextButton(
        onClick = {
            abierto = true
        },
        modifier = modifier
    ) {
        Text(
            "♿ Ajustes de accesibilidad",
            fontWeight = FontWeight.Bold
        )
    }

    if (abierto) {
        AlertDialog(
            onDismissRequest = {
                abierto = false
            },
            title = {
                Text("Accesibilidad de MPAZ")
            },
            text = {
                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    OpcionAccesibilidadMPAZ(
                        titulo = "Texto más grande",
                        activada = ajustes.textoGrande,
                        onCambiar =
                            ajustes::cambiarTextoGrande
                    )

                    OpcionAccesibilidadMPAZ(
                        titulo = "Alto contraste en preguntas",
                        activada = ajustes.altoContraste,
                        onCambiar =
                            ajustes::cambiarContraste
                    )

                    OpcionAccesibilidadMPAZ(
                        titulo = "Alternativas más grandes",
                        activada = ajustes.botonesGrandes,
                        onCambiar =
                            ajustes::cambiarBotones
                    )

                    Text(
                        text = "Estos ajustes se guardan " +
                                "en este dispositivo.",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        abierto = false
                    }
                ) {
                    Text("Listo")
                }
            }
        )
    }
}

@Composable
private fun OpcionAccesibilidadMPAZ(
    titulo: String,
    activada: Boolean,
    onCambiar: (Boolean) -> Unit
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        Switch(
            checked = activada,
            onCheckedChange = onCambiar
        )
    }
}