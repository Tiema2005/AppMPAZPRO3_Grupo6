package com.example.appmpazpro3_grupo6.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val MPAZLightColorScheme = lightColorScheme(
    primary = MPAZCoral,
    onPrimary = MPAZBlanco,

    primaryContainer = MPAZCoralSuave,
    onPrimaryContainer = MPAZTexto,

    secondary = MPAZAzul,
    onSecondary = MPAZBlanco,

    secondaryContainer = MPAZAzulSuave,
    onSecondaryContainer = MPAZTexto,

    tertiary = MPAZVerde,
    onTertiary = MPAZTexto,

    tertiaryContainer = MPAZVerdeSuave,
    onTertiaryContainer = MPAZTexto,

    background = MPAZCrema,
    onBackground = MPAZTexto,

    surface = MPAZBlanco,
    onSurface = MPAZTexto,

    surfaceVariant = MPAZCrema,
    onSurfaceVariant = MPAZTextoSecundario,

    outline = MPAZTextoSecundario,
    outlineVariant = MPAZBorde,

    error = MPAZError,
    onError = MPAZBlanco
)

private val MPAZDarkColorScheme = darkColorScheme(
    primary = MPAZCoralSuave,
    onPrimary = MPAZFondoOscuro,

    secondary = MPAZAzulSuave,
    onSecondary = MPAZFondoOscuro,

    tertiary = MPAZVerdeSuave,
    onTertiary = MPAZFondoOscuro,

    background = MPAZFondoOscuro,
    onBackground = MPAZTextoClaro,

    surface = MPAZSuperficieOscura,
    onSurface = MPAZTextoClaro,

    surfaceVariant = MPAZSuperficieOscura,
    onSurfaceVariant = MPAZTextoClaro
)

@Composable
fun AppMPAZPRO3_Grupo6Theme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current

            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> MPAZDarkColorScheme

        else -> MPAZLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}