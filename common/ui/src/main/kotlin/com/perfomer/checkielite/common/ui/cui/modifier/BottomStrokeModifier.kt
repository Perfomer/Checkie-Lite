package com.perfomer.checkielite.common.ui.cui.modifier

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.bottomStroke(
    strokeColor: Color,
    strokeWidth: Dp = 1.dp,
    strokeAlpha: Float = 1F,
): Modifier {
    return this then drawWithContent {
        drawContent()
        drawLine(
            color = strokeColor,
            start = Offset(x = 0F, y = size.height),
            end = Offset(x = size.width, y = size.height),
            strokeWidth = strokeWidth.toPx(),
            alpha = strokeAlpha,
        )
    }
}

@Composable
fun Modifier.bottomStrokeOnScroll(
    scrollableState: ScrollableState,
    strokeColor: Color,
    strokeWidth: Dp = 1.dp,
    animationSpec: AnimationSpec<Float> = tween(250),
): Modifier {
    val strokeAlpha = remember { Animatable(1F) }

    LaunchedEffect(scrollableState.canScrollBackward) {
        val targetAlpha = if (scrollableState.canScrollBackward) 1F else 0F
        strokeAlpha.animateTo(targetValue = targetAlpha, animationSpec = animationSpec)
    }

    return this then bottomStroke(
        strokeColor = strokeColor,
        strokeWidth = strokeWidth,
        strokeAlpha = strokeAlpha.value,
    )
}