package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.tooling.preview.Preview
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
