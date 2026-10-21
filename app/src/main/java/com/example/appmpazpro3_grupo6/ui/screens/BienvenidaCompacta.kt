package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun BienvenidaCompacta(
    onComenzarClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(45.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "MPAZ PRO 3.º",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0066CC),
            textAlign = TextAlign.Center
        )

        Text(
            text = "📖",
            fontSize = 90.sp
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "¡Bienvenido!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003366)
            )

            Text(
                text = "Escuela Escritora Marcela Paz",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Tu plataforma de aprendizaje para Tercero Básico",
                fontSize = 17.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = onComenzarClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0066CC)
            )
        ) {
            Text(
                text = "Comenzar →",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(
    name = "Vista compacta - Pixel 6",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PreviewBienvenidaCompacta() {
    MaterialTheme {
        BienvenidaCompacta()
    }
}