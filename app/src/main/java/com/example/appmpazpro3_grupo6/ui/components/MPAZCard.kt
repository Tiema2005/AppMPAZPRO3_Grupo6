package com.example.appmpazpro3_grupo6.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmpazpro3_grupo6.ui.theme.MPAZBlanco
import com.example.appmpazpro3_grupo6.ui.theme.MPAZTexto

@Composable
fun MPAZCard(
    modifier: Modifier = Modifier,
    fondo: Color = MPAZBlanco,
    onClick: (() -> Unit)? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(22.dp)

    val colores = CardDefaults.elevatedCardColors(
        containerColor = fondo
    )

    val elevacion = CardDefaults.elevatedCardElevation(
        defaultElevation = 3.dp
    )

    if (onClick != null) {
        ElevatedCard(
            onClick = onClick,
            modifier = modifier,
            shape = forma,
            colors = colores,
            elevation = elevacion
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                content = contenido
            )
        }
    } else {
        ElevatedCard(
            modifier = modifier,
            shape = forma,
            colors = colores,
            elevation = elevacion
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                content = contenido
            )
        }
    }
}

@Composable
fun MPAZAsignaturaCard(
    titulo: String,
    emoji: String,
    fondo: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MPAZCard(
        modifier = modifier.heightIn(min = 145.dp),
        fondo = fondo,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = emoji,
                fontSize = 43.sp
            )

            Text(
                text = titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MPAZTexto,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}