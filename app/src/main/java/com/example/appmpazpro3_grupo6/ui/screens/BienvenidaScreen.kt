package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appmpazpro3_grupo6.ui.theme.MPAZBorde
import com.example.appmpazpro3_grupo6.ui.theme.MPAZCoral
import com.example.appmpazpro3_grupo6.ui.utils.obtenerWindowSizeClass

@Composable
fun BienvenidaScreen(
    onComenzarClick: () -> Unit = {}
) {
    val windowSizeClass = obtenerWindowSizeClass()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (windowSizeClass.widthSizeClass) {

            WindowWidthSizeClass.Compact -> {
                BienvenidaCompacta(
                    onComenzarClick = onComenzarClick
                )
            }

            WindowWidthSizeClass.Medium -> {
                BienvenidaMediana(
                    onComenzarClick = onComenzarClick
                )
            }

            WindowWidthSizeClass.Expanded -> {
                BienvenidaExpandida(
                    onComenzarClick = onComenzarClick
                )
            }

            else -> {
                BienvenidaCompacta(
                    onComenzarClick = onComenzarClick
                )
            }
        }
    }
}

@Composable
internal fun IndicadoresBienvenida() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(10.dp)
                .background(MPAZCoral, CircleShape)
        )

        repeat(2) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(MPAZBorde, CircleShape)
            )
        }
    }
}