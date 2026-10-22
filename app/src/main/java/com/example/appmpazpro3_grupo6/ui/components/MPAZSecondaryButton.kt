package com.example.appmpazpro3_grupo6.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmpazpro3_grupo6.ui.theme.MPAZCoral

@Composable
fun MPAZSecondaryButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(50.dp),
        border = BorderStroke(1.5.dp, MPAZCoral),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MPAZCoral
        )
    ) {
        Text(
            text = texto,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}