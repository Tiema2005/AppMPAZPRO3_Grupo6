package com.example.appmpazpro3_grupo6.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appmpazpro3_grupo6.ui.theme.MPAZAzul
import com.example.appmpazpro3_grupo6.ui.theme.MPAZAzulSuave
import com.example.appmpazpro3_grupo6.ui.theme.MPAZTexto

@Composable
fun MPAZProgress(
    progreso: Float,
    modifier: Modifier = Modifier,
    etiqueta: String = "Mi progreso"
) {
    val valor = progreso.coerceIn(0f, 1f)
    val porcentaje = (valor * 100).toInt()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "$etiqueta: $porcentaje%",
            color = MPAZTexto,
            fontWeight = FontWeight.SemiBold
        )

        LinearProgressIndicator(
            progress = { valor },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(CircleShape),
            color = MPAZAzul,
            trackColor = MPAZAzulSuave
        )
    }
}