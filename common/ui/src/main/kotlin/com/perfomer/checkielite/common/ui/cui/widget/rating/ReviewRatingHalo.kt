package com.perfomer.checkielite.common.ui.cui.widget.rating

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

/** Draw outside the rating bounds; the caller keeps the ripple on a separate, clipped layer. */
@Composable
fun ReviewRatingHalo(
    centerFromStart: Dp,
    modifier: Modifier = Modifier
) {
    val isDark = LocalCuiPalette.current.BackgroundElevationBase.luminance() < 0.5F
    val layoutDirection = LocalLayoutDirection.current
    val transition = rememberInfiniteTransition(label = "Diamond halo")
    val opacity = transition.animateFloat(
        initialValue = 0.85F,
        targetValue = 1F,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3250),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "Diamond halo breath",
    )

    Box(
        modifier = modifier.drawWithCache {
            val center = Offset(
                x = if (layoutDirection == LayoutDirection.Ltr) centerFromStart.toPx() else size.width - centerFromStart.toPx(),
                y = size.height / 2F,
            )
            val radius = 40.dp.toPx()
            val halo = Brush.radialGradient(
                0F to Color(0xFFF4FCFF).copy(alpha = if (isDark) 0.30F else 0.80F),
                0.20F to Color(0xFFB9E8FF).copy(alpha = if (isDark) 0.30F else 0.64F),
                0.40F to Color(0xFF79CEFF).copy(alpha = if (isDark) 0.27F else 0.46F),
                0.60F to Color(0xFF41E3EB).copy(alpha = if (isDark) 0.15F else 0.24F),
                0.80F to Color(0xFF929BFF).copy(alpha = if (isDark) 0.05F else 0.08F),
                0.92F to Color(0xFF929BFF).copy(alpha = 0.015F),
                1F to Color(0xFF929BFF).copy(alpha = 0F),
                center = center,
                radius = radius,
            )

            // No offscreen layer or clipping: every edge fades to transparent before it ends.
            onDrawBehind {
                drawCircle(brush = halo, radius = radius, center = center, alpha = opacity.value)
            }
        }
    )
}
