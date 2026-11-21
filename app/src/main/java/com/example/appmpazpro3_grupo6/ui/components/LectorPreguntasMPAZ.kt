package com.example.appmpazpro3_grupo6.ui.components

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.appmpazpro3_grupo6.ui.theme.MPAZCoral
import com.example.appmpazpro3_grupo6.ui.theme.MPAZError
import com.example.appmpazpro3_grupo6.ui.theme.MPAZTextoSecundario
import java.util.Locale

@Composable
fun LectorPreguntasMPAZ(
    pregunta: String,
    opciones: List<String>,
    explicacion: String?,
    respuestaCorrecta: String?,
    clavePregunta: String,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current.applicationContext
    val vistaPrevia = LocalInspectionMode.current
    val propietario = LocalLifecycleOwner.current

    var motor by remember {
        mutableStateOf<TextToSpeech?>(null)
    }

    var disponible by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }

    var lecturaLenta by rememberSaveable {
        mutableStateOf(false)
    }

    DisposableEffect(contexto, vistaPrevia) {
        if (vistaPrevia) {
            onDispose { }
        } else {
            var activo = true

            val instancia = TextToSpeech(contexto) { estado ->
                if (activo) {
                    disponible = estado == TextToSpeech.SUCCESS

                    if (!disponible) {
                        error = "No se pudo iniciar la voz de Android."
                    }
                }
            }

            motor = instancia

            onDispose {
                activo = false
                instancia.stop()
                instancia.shutdown()
                motor = null
                disponible = false
            }
        }
    }

    DisposableEffect(propietario, motor) {
        val observador = LifecycleEventObserver { _, evento ->
            if (evento == Lifecycle.Event.ON_STOP) {
                motor?.stop()
            }
        }

        propietario.lifecycle.addObserver(observador)

        onDispose {
            propietario.lifecycle.removeObserver(observador)
        }
    }

    LaunchedEffect(clavePregunta, explicacion) {
        motor?.stop()
    }

    fun reproducir(texto: String) {
        val voz = motor

        if (!disponible || voz == null) {
            error = "El lector de voz todavía no está disponible."
            return
        }

        var idioma = voz.setLanguage(
            Locale.forLanguageTag("es-CL")
        )

        if (idioma < TextToSpeech.LANG_AVAILABLE) {
            idioma = voz.setLanguage(
                Locale.forLanguageTag("es-ES")
            )
        }

        if (idioma < TextToSpeech.LANG_AVAILABLE) {
            idioma = voz.setLanguage(
                Locale.forLanguageTag("es")
            )
        }

        if (idioma < TextToSpeech.LANG_AVAILABLE) {
            error = "Instala una voz en español en los ajustes de Android."
            return
        }

        voz.setSpeechRate(
            if (lecturaLenta) 0.72f else 0.95f
        )

        val resultado = voz.speak(
            texto,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "mpaz_lectura"
        )

        error = if (resultado == TextToSpeech.ERROR) {
            "No se pudo reproducir el audio."
        } else {
            null
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        TextButton(
            onClick = {
                val lectura = buildString {
                    append("Pregunta. ")
                    append(pregunta)
                    append(". ")

                    opciones.forEachIndexed { indice, opcion ->
                        append("Alternativa ${indice + 1}. ")
                        append(opcion)
                        append(". ")
                    }
                }

                reproducir(lectura)
            },
            enabled = disponible
        ) {
            Text(
                text = "🔊 Escuchar pregunta y alternativas",
                fontWeight = FontWeight.Bold,
                color = if (disponible) {
                    MPAZCoral
                } else {
                    MPAZTextoSecundario
                },
                textAlign = TextAlign.Center
            )
        }

        if (
            explicacion != null &&
            respuestaCorrecta != null
        ) {

            TextButton(
                onClick = {
                    reproducir(
                        "La respuesta correcta es $respuestaCorrecta. $explicacion"
                    )
                },
                enabled = disponible
            ) {
                Text(
                    text = "🔊 Escuchar explicación",
                    fontWeight = FontWeight.Bold,
                    color = if (disponible) {
                        MPAZCoral
                    } else {
                        MPAZTextoSecundario
                    }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                onClick = {
                    motor?.stop()
                },
                enabled = disponible
            ) {
                Text(
                    text = "⏹ Detener",
                    fontSize = 13.sp
                )
            }

            TextButton(
                onClick = {
                    lecturaLenta = !lecturaLenta
                }
            ) {
                Text(
                    text = if (lecturaLenta) {
                        "🐢 Voz lenta ✓"
                    } else {
                        "🐢 Voz lenta"
                    },
                    fontSize = 13.sp
                )
            }
        }

        if (
            !disponible &&
            error == null &&
            !vistaPrevia
        ) {
            Text(
                text = "Preparando lector de voz…",
                fontSize = 12.sp,
                color = MPAZTextoSecundario,
                textAlign = TextAlign.Center
            )
        }

        error?.let { mensaje ->
            Text(
                text = mensaje,
                fontSize = 12.sp,
                color = MPAZError,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}