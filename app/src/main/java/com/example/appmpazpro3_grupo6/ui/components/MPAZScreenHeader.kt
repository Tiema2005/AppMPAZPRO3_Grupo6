package com.example.appmpazpro3_grupo6.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmpazpro3_grupo6.ui.theme.MPAZCoral
import com.example.appmpazpro3_grupo6.ui.theme.MPAZTexto
import com.example.appmpazpro3_grupo6.ui.theme.MPAZTextoSecundario

@Composable
fun MPAZScreenHeader(
    titulo: String,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    onVolverClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (onVolverClick != null) {
            TextButton(
                onClick = onVolverClick
            ) {
                Text(
                    text = "←",
                    fontSize = 24.sp,
                    color = MPAZCoral
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = titulo,
                fontSize = 27.sp,
                fontWeight = FontWeight.Black,
                color = MPAZTexto,
                textAlign = TextAlign.Start
            )

            if (!subtitulo.isNullOrBlank()) {
                Text(
                    text = subtitulo,
                    fontSize = 15.sp,
                    color = MPAZTextoSecundario
                )
            }
        }
    }
}