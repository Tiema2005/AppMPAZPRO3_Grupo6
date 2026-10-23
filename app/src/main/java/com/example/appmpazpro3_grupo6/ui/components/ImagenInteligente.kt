package com.example.appmpazpro3_grupo6.ui.components

import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.appmpazpro3_grupo6.ui.theme.*

@Composable
fun ImagenInteligente(
    imagenUri: Uri?,
    modifier: Modifier = Modifier.size(110.dp)
) {

    val contexto = LocalContext.current

    val imagen by produceState<ImageBitmap?>(
        initialValue = null,
        key1 = imagenUri
    ) {

        value = if (imagenUri != null) {

            withContext(Dispatchers.IO) {

                runCatching {

                    val origen = ImageDecoder.createSource(
                        contexto.contentResolver,
                        imagenUri
                    )

                    ImageDecoder.decodeBitmap(origen) {
                            decoder, info, _ ->

                        val dimensionMayor = maxOf(
                            info.size.width,
                            info.size.height
                        )

                        decoder.setTargetSampleSize(
                            (dimensionMayor / 512)
                                .coerceAtLeast(1)
                        )
                    }.asImageBitmap()

                }.getOrNull()
            }

        } else {
            null
        }
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MPAZAzulSuave),
        contentAlignment = Alignment.Center
    ) {

        val imagenActual = imagen

        if (imagenActual != null) {

            Image(
                bitmap = imagenActual,
                contentDescription = "Fotografía del estudiante",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

        } else {

            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Perfil sin fotografía",
                tint = MPAZAzul,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}

@Preview(
    name = "MPAZ - Imagen inteligente",
    showBackground = true
)
@Composable
fun PreviewImagenInteligente() {

    AppMPAZPRO3_Grupo6Theme {
        ImagenInteligente(
            imagenUri = null
        )
    }
}