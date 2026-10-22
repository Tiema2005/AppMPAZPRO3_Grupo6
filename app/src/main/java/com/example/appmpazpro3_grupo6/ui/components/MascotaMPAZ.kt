package com.example.appmpazpro3_grupo6.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.appmpazpro3_grupo6.R
import com.example.appmpazpro3_grupo6.ui.theme.AppMPAZPRO3_Grupo6Theme

@Composable
fun MascotaMPAZ(
    modifier: Modifier = Modifier.size(235.dp)
) {
    Image(
        painter = painterResource(
            id = R.drawable.buho
        ),
        contentDescription = "Búho de MPAZ PRO 3.º",
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

@Preview(
    name = "Mascota MPAZ",
    showBackground = true,
    widthDp = 300,
    heightDp = 300
)
@Composable
fun PreviewMascotaMPAZ() {
    AppMPAZPRO3_Grupo6Theme {
        MascotaMPAZ()
    }
}
