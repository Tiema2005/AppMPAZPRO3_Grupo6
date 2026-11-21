package com.example.appmpazpro3_grupo6.ui.components

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.appmpazpro3_grupo6.ui.theme.MPAZCoral
import com.example.appmpazpro3_grupo6.ui.theme.MPAZTextoSecundario
import java.text.Normalizer
import java.util.Locale

private fun textoNormalizado(texto: String): String =
    Normalizer.normalize(
        texto.lowercase(Locale.ROOT),
        Normalizer.Form.NFD
    )
        .replace(Regex("\\p{M}+"), "")
        .replace(Regex("[^a-z0-9 ]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()

private val numerosHablados = mapOf(
    "cero" to "0",
    "uno" to "1",
    "un" to "1",
    "una" to "1",
    "dos" to "2",
    "tres" to "3",
    "cuatro" to "4",
    "cinco" to "5",
    "seis" to "6",
    "siete" to "7",
    "ocho" to "8",
    "nueve" to "9",
    "diez" to "10",
    "once" to "11",
    "doce" to "12",
    "trece" to "13",
    "catorce" to "14",
    "quince" to "15",
    "dieciseis" to "16",
    "diecisiete" to "17",
    "dieciocho" to "18",
    "diecinueve" to "19",
    "veinte" to "20",
    "veintiuno" to "21",
    "veintidos" to "22",
    "veintitres" to "23",
    "veinticuatro" to "24",
    "veinticinco" to "25",
    "treinta" to "30",
    "cuarenta" to "40",
    "cincuenta" to "50",
    "sesenta" to "60",
    "setenta" to "70",
    "ochenta" to "80",
    "noventa" to "90",
    "cien" to "100"
)

private val posicionesHabladas = mapOf(
    "uno" to 0,
    "un" to 0,
    "1" to 0,
    "a" to 0,
    "primera" to 0,
    "primero" to 0,
    "dos" to 1,
    "2" to 1,
    "b" to 1,
    "segunda" to 1,
    "segundo" to 1,
    "tres" to 2,
    "3" to 2,
    "c" to 2,
    "tercera" to 2,
    "tercero" to 2
)

private fun reconocerAlternativa(
    frases: List<String>,
    opciones: List<String>
): String? {

    val muletillas = Regex(
        "\\b(yo|creo|que|es|la|el|los|las|" +
                "respuesta|correcta|marco|elijo|escojo|" +
                "quiero|seria|voy|con|selecciono|mi)\\b"
    )

    val referencia = Regex(
        "\\b(?:opcion|alternativa|numero|respuesta)\\s+" +
                "(?:numero\\s+)?(uno|un|dos|tres|" +
                "primera|segunda|tercera|primero|segundo|" +
                "tercero|1|2|3|a|b|c)\\b"
    )

    for (frase in frases) {

        val hablada = textoNormalizado(frase)

        if (hablada.isBlank()) {
            continue
        }

        val posicionExplicita = referencia
            .find(hablada)
            ?.groupValues
            ?.getOrNull(1)
            ?.let { posicionesHabladas[it] }

        if (posicionExplicita != null) {
            opciones.getOrNull(posicionExplicita)?.let {
                return it
            }
        }

        val simplificada = textoNormalizado(
            hablada.replace(muletillas, " ")
        )

        for (
        candidata in listOf(
            hablada,
            simplificada
        ).distinct()
        ) {

            val equivalente =
                numerosHablados[candidata] ?: candidata

            opciones.firstOrNull { opcion ->
                val valor = textoNormalizado(opcion)

                valor == candidata ||
                        valor == equivalente
            }?.let {
                return it
            }
        }

        val posicion = posicionesHabladas[simplificada]

        if (posicion != null) {
            opciones.getOrNull(posicion)?.let {
                return it
            }
        }

        val coincidencias = opciones.filter { opcion ->

            val valor = textoNormalizado(opcion)
            val palabras = hablada.split(" ")
            val equivalente = numerosHablados[valor] ?: valor

            val fraseExacta =
                valor.length >= 4 &&
                        Regex(
                            "(?:^| )${Regex.escape(valor)}(?:$| )"
                        ).containsMatchIn(hablada)

            val numeroExacto =
                equivalente.isNotBlank() &&
                        equivalente.all(Char::isDigit) &&
                        palabras.contains(equivalente)

            fraseExacta || numeroExacto
        }

        if (coincidencias.size == 1) {
            return coincidencias.first()
        }
    }

    return null
}

@Composable
fun RespuestaPorVozMPAZ(
    opciones: List<String>,
    habilitado: Boolean,
    clavePregunta: String,
    onRespuesta: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val contexto = LocalContext.current
    val vistaPrevia = LocalInspectionMode.current

    val callbackActual by rememberUpdatedState(onRespuesta)
    val habilitadoActual by rememberUpdatedState(habilitado)
    val opcionesActuales by rememberUpdatedState(opciones)

    var reconocedor by remember {
        mutableStateOf<SpeechRecognizer?>(null)
    }

    var escuchando by remember {
        mutableStateOf(false)
    }

    var aviso by remember {
        mutableStateOf<String?>(null)
    }

    var usarEspanolGeneral by remember {
        mutableStateOf(false)
    }

    DisposableEffect(
        contexto,
        clavePregunta,
        vistaPrevia
    ) {

        if (vistaPrevia) {

            onDispose { }

        } else if (
            !SpeechRecognizer.isRecognitionAvailable(contexto)
        ) {

            aviso =
                "No hay un servicio de reconocimiento de voz instalado."

            onDispose { }

        } else {

            val servicio =
                SpeechRecognizer.createSpeechRecognizer(contexto)

            servicio.setRecognitionListener(
                object : RecognitionListener {

                    override fun onReadyForSpeech(
                        params: Bundle?
                    ) {
                        escuchando = true

                        aviso =
                            "Te escucho. Di 'opción dos' " +
                                    "o el nombre de la respuesta."
                    }

                    override fun onBeginningOfSpeech() = Unit

                    override fun onRmsChanged(
                        rmsdB: Float
                    ) = Unit

                    override fun onBufferReceived(
                        buffer: ByteArray?
                    ) = Unit

                    override fun onEndOfSpeech() {
                        aviso = "Interpretando la respuesta…"
                    }

                    override fun onError(error: Int) {

                        escuchando = false

                        aviso = when (error) {

                            SpeechRecognizer.ERROR_NO_MATCH,
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                                "No se entendió. Intenta decir " +
                                        "'opción dos' con claridad."

                            SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED,
                            SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE -> {

                                usarEspanolGeneral = true

                                "No está disponible español de Chile. " +
                                        "Prueba nuevamente."
                            }

                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                                "Debes autorizar el micrófono en Android."

                            SpeechRecognizer.ERROR_NETWORK,
                            SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                                "El servicio de voz necesita conexión. " +
                                        "Revisa Internet."

                            else ->
                                "El reconocimiento falló " +
                                        "(código $error). Intenta nuevamente."
                        }
                    }

                    override fun onResults(
                        results: Bundle?
                    ) {

                        escuchando = false

                        val frases = results
                            ?.getStringArrayList(
                                SpeechRecognizer.RESULTS_RECOGNITION
                            )
                            .orEmpty()

                        val reconocida =
                            frases.firstOrNull().orEmpty()

                        val respuesta = reconocerAlternativa(
                            frases,
                            opcionesActuales
                        )

                        if (
                            respuesta != null &&
                            habilitadoActual
                        ) {

                            aviso =
                                "Escuché: $reconocida. " +
                                        "Seleccioné: $respuesta"

                            callbackActual(respuesta)

                        } else {

                            aviso = if (frases.isEmpty()) {

                                "No se recibió texto. " +
                                        "Inténtalo nuevamente."

                            } else {

                                "Android entendió: '$reconocida'. " +
                                        "Di 'opción uno, dos o tres'."
                            }
                        }
                    }

                    override fun onPartialResults(
                        partialResults: Bundle?
                    ) = Unit

                    override fun onEvent(
                        eventType: Int,
                        params: Bundle?
                    ) = Unit
                }
            )

            reconocedor = servicio

            onDispose {

                servicio.cancel()
                servicio.destroy()

                reconocedor = null
                escuchando = false
            }
        }
    }

    LaunchedEffect(
        habilitado,
        clavePregunta
    ) {

        if (!habilitado) {
            reconocedor?.cancel()
            escuchando = false
        }
    }

    fun iniciarReconocimiento() {

        if (!habilitado || escuchando) {
            return
        }

        val servicio = reconocedor ?: run {
            aviso = "El reconocimiento de voz no está disponible."
            return
        }

        try {

            val intencion = Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH
            ).apply {

                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )

                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    if (usarEspanolGeneral) {
                        "es-ES"
                    } else {
                        "es-CL"
                    }
                )

                putExtra(
                    RecognizerIntent.EXTRA_MAX_RESULTS,
                    5
                )
            }

            escuchando = true
            aviso = "Preparando micrófono…"

            servicio.startListening(intencion)

        } catch (_: Exception) {

            escuchando = false
            aviso = "No se pudo activar el micrófono."
        }
    }

    val solicitarPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->

        if (concedido) {
            iniciarReconocimiento()
        } else {
            aviso =
                "Activa el permiso de micrófono " +
                        "para responder por voz."
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        TextButton(
            onClick = {

                if (
                    ContextCompat.checkSelfPermission(
                        contexto,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                ) {

                    iniciarReconocimiento()

                } else {

                    solicitarPermiso.launch(
                        Manifest.permission.RECORD_AUDIO
                    )
                }
            },
            enabled =
                habilitado &&
                        reconocedor != null &&
                        !escuchando
        ) {

            Text(
                text = if (escuchando) {
                    "🎤 Escuchando…"
                } else {
                    "🎤 Responder por voz"
                },
                color = MPAZCoral,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        if (escuchando) {

            TextButton(
                onClick = {

                    reconocedor?.cancel()
                    escuchando = false
                    aviso = "Escucha cancelada."
                }
            ) {
                Text("Cancelar")
            }
        }

        aviso?.let { mensaje ->

            Text(
                text = mensaje,
                fontSize = 13.sp,
                color = MPAZTextoSecundario,
                textAlign = TextAlign.Center
            )
        }
    }
}