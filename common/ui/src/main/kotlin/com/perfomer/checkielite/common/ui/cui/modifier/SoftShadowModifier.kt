package com.perfomer.checkielite.common.ui.cui.modifier

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
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

/** Shares [interactionSource] with the clickable element to fade its shadow while pressed. */
@Composable
fun Modifier.softShadow(
    interactionSource: InteractionSource,
    shape: Shape,
    radius: Dp = 12.dp,
    offset: DpOffset = DpOffset(x = 0.dp, y = 3.dp),
    color: Color = Color.Black.copy(alpha = 0.08F),
): Modifier {
    val isPressed by interactionSource.collectIsPressedAsState()
    val shadowAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0F else 1F,
        // Match the incoming and outgoing elevation animation of Material buttons.
        animationSpec = if (isPressed) {
            tween(durationMillis = 120, easing = FastOutSlowInEasing)
        } else {
            tween(durationMillis = 150, easing = ShadowReleaseEasing)
        },
        label = "SoftShadowAlpha",
    )

    return dropShadow(shape) {
        this.radius = radius.toPx()
        this.offset = Offset(offset.x.toPx(), offset.y.toPx())
        this.color = color
        alpha = shadowAlpha
    }
}

private val ShadowReleaseEasing = CubicBezierEasing(0.4F, 0F, 0.6F, 1F)
