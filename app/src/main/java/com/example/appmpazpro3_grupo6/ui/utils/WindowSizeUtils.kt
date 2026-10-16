package com.example.appmpazpro3_grupo6.ui.utils

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerWindowSizeClass(): WindowSizeClass {

    val activity = LocalActivity.current
        ?: error("Se necesita una Activity para calcular el tamaño")

    return calculateWindowSizeClass(activity)
}
