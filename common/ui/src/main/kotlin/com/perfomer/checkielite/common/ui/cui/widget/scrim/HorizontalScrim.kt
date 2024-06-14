package com.perfomer.checkielite.common.ui.cui.widget.scrim

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

@Composable
fun HorizontalScrim(
    modifier: Modifier = Modifier,
    color: Color = LocalCuiPalette.current.BackgroundPrimary,
    fromLeft: Boolean = false,
) {
    Box(modifier = modifier.background(horizontalScrimBrush(color, fromLeft)))
}

@Composable
fun horizontalScrimBrush(
    color: Color = LocalCuiPalette.current.BackgroundPrimary,
    fromLeft: Boolean = false,
): Brush {
    return remember(color, fromLeft) {
        Brush.horizontalGradient(
            if (fromLeft) listOf(color, color.copy(alpha = 0F))
            else listOf(color.copy(alpha = 0F), color)
        )
    }
}