package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
internal fun ReviewInfoScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.Yellow)
    ) {
        Text(text = "Production", fontSize = 36.sp)
        Text(text = "Production", fontSize = 36.sp)
        Text(text = "Production", fontSize = 36.sp)
        Text(text = "Production", fontSize = 36.sp)
        Text(text = "Production", fontSize = 36.sp)
        Text(text = "Production", fontSize = 36.sp)
        Text(text = "Production", fontSize = 36.sp)
    }
}