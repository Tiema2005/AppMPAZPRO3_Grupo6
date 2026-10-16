package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BienvenidaMediana(
    onComenzarClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Text(
            text = "MPAZ PRO 3.º",
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0066CC)
        )

        Text(
            text = "📖",
            fontSize = 105.sp
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "¡Bienvenido!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003366)
            )

            Text(
                text = "Escuela Escritora Marcela Paz",
                fontSize = 23.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Tu plataforma de aprendizaje para Tercero Básico",
                fontSize = 19.sp,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = onComenzarClick,
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Comenzar →", fontSize = 20.sp)
        }
    }
}

@Preview(
    name = "Vista mediana",
    showBackground = true,
    widthDp = 700,
    heightDp = 900
)
@Composable
fun PreviewBienvenidaMediana() {
    MaterialTheme {
        BienvenidaMediana()
    }
}