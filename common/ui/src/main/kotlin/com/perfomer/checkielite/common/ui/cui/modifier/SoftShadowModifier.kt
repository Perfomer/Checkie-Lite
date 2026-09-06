package com.perfomer.checkielite.common.ui.cui.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

fun Modifier.softShadow(
    shape: Shape,
    radius: Dp = 12.dp,
    offset: DpOffset = DpOffset(x = 0.dp, y = 3.dp),
    color: Color = Color.Black.copy(alpha = 0.08F),
): Modifier = dropShadow(
    shape = shape,
    shadow = Shadow(
        radius = radius,
        color = color,
        offset = offset,
    ),
)
