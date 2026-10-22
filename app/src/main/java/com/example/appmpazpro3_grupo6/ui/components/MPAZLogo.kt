package com.example.appmpazpro3_grupo6.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmpazpro3_grupo6.ui.theme.MPAZAzul
import com.example.appmpazpro3_grupo6.ui.theme.MPAZCoral
import com.example.appmpazpro3_grupo6.ui.theme.MPAZTexto

@Composable
fun MPAZLogo(
    modifier: Modifier = Modifier,
    compacto: Boolean = false,
    mostrarLema: Boolean = false
) {
    val tamanoMPAZ = if (compacto) 33.sp else 49.sp
    val tamanoPRO = if (compacto) 29.sp else 43.sp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MPAZ",
            fontSize = tamanoMPAZ,
            fontWeight = FontWeight.Black,
            color = MPAZCoral,
            textAlign = TextAlign.Center,
            lineHeight = if (compacto) 38.sp else 52.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "PRO",
                fontSize = tamanoPRO,
                fontWeight = FontWeight.Black,
                color = MPAZAzul
            )

            Text(
                text = "3.º",
                fontSize = tamanoPRO,
                fontWeight = FontWeight.Black,
                color = Color(0xFF2AA966)
            )
        }

        if (mostrarLema) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = "¡Aprender es una aventura!",
                color = MPAZTexto,
                fontSize = if (compacto) 16.sp else 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}