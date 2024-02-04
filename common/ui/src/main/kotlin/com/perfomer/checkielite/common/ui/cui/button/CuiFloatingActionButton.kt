package com.perfomer.checkielite.common.ui.cui.button

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun CuiFloatingActionButton(
    painter: Painter,
    contentDescription: String? = null,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        shape = CircleShape,
        onClick = onClick,
    ) {
        Icon(painter = painter, contentDescription = contentDescription)
    }
}