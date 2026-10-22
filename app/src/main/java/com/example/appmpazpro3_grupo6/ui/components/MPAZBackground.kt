package com.example.appmpazpro3_grupo6.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.appmpazpro3_grupo6.ui.theme.MPAZAmarilloSuave
import com.example.appmpazpro3_grupo6.ui.theme.MPAZCrema

@Composable
fun MPAZBackground(
    modifier: Modifier = Modifier,
    contenido: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MPAZCrema)
    ) {

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-55).dp, y = (-50).dp)
                .size(145.dp)
                .clip(CircleShape)
                .background(MPAZAmarilloSuave)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 65.dp, y = 55.dp)
                .size(175.dp)
                .clip(CircleShape)
                .background(MPAZAmarilloSuave)
        )
        contenido()
    }
}
