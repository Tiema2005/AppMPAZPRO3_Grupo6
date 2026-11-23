package com.example.appmpazpro3_grupo6.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmpazpro3_grupo6.ui.theme.MPAZCoral
import com.example.appmpazpro3_grupo6.ui.theme.MPAZTexto
import com.example.appmpazpro3_grupo6.ui.theme.MPAZTextoSecundario
import com.example.appmpazpro3_grupo6.viewmodel.EstadoLecturaCiencias
import com.example.appmpazpro3_grupo6.viewmodel.LecturasCienciasViewModel

@Composable
fun MPAZLecturaCiencias(
    articulo: String,
    modifier: Modifier = Modifier
) {
    val lecturasViewModel: LecturasCienciasViewModel = viewModel(
        key = "lectura:$articulo"
    ) {
        LecturasCienciasViewModel(articulo = articulo)
    }

    val estado by lecturasViewModel.estado.collectAsState()
    val uriHandler = LocalUriHandler.current

    MPAZLecturaCienciasContenido(
        estado = estado,
        onAbrirWikipedia = { url ->
            uriHandler.openUri(url)
        },
        onReintentar = {
            lecturasViewModel.cargarLectura()
        },
        modifier = modifier
    )
}

@Composable
fun MPAZLecturaCienciasContenido(
    estado: EstadoLecturaCiencias,
    onAbrirWikipedia: (String) -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    MPAZCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Para aprender más",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MPAZTexto
            )

            when (estado) {

                EstadoLecturaCiencias.Cargando -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MPAZCoral
                    )

                    Text(
                        text = "Cargando lectura…",
                        color = MPAZTextoSecundario
                    )
                }

                is EstadoLecturaCiencias.Disponible -> {
                    val lectura = estado.lectura

                    Text(
                        text = lectura.titulo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MPAZTexto
                    )

                    if (!lectura.descripcion.isNullOrBlank()) {
                        Text(
                            text = lectura.descripcion,
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = MPAZTextoSecundario
                        )
                    }

                    TextButton(
                        onClick = {
                            onAbrirWikipedia(lectura.urlFuente)
                        }
                    ) {
                        Text("Leer en Wikipedia")
                    }
                }

                EstadoLecturaCiencias.SinResultados -> {
                    Text(
                        text = "La lectura no está disponible por ahora.",
                        color = MPAZTextoSecundario
                    )

                    TextButton(onClick = onReintentar) {
                        Text("Reintentar")
                    }
                }

                EstadoLecturaCiencias.Error -> {
                    Text(
                        text = "No pudimos cargar la lectura. Revisa tu conexión.",
                        color = MPAZTextoSecundario
                    )

                    TextButton(onClick = onReintentar) {
                        Text("Reintentar")
                    }
                }
            }
        }
    }
}