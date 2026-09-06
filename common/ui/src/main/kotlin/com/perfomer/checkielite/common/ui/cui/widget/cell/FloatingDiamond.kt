package com.perfomer.checkielite.common.ui.cui.widget.cell

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.widget.rating.ReviewReaction
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FloatingDiamond(modifier: Modifier = Modifier) {
    val isDark = LocalCuiPalette.current.BackgroundElevationBase.luminance() < 0.5F
    val transition = rememberInfiniteTransition(label = "Floating diamond")
    val phase = transition.animateFloat(
        initialValue = 0F,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Diamond breath",
    )

    // Keep the original rating footprint; only drawing and the icon layer move.
    Box(
        modifier = modifier
            .size(28.dp)
            .drawWithCache {
                val shadow = Brush.radialGradient(
                    0F to Color(0xFF17456B),
                    0.35F to Color(0xFF17456B).copy(alpha = 0.65F),
                    1F to Color(0xFF17456B).copy(alpha = 0F),
                    center = Offset.Zero,
                    radius = 1F,
                )
                val shadowCenter = Offset(size.width / 2F, size.height + 1.dp.toPx())
                val particleColor = if (isDark) Color(0xFFD5F7FF) else Color(0xFF4A9CCD)

                onDrawBehind {
                    val angle = phase.value
                    val lift = (1F - cos(angle)) / 2F
                    val shadowRadiusX = (10F - lift * 3F).dp.toPx()
                    val shadowRadiusY = (2.5F - lift * 0.8F).dp.toPx()
                    withTransform({
                        translate(left = shadowCenter.x, top = shadowCenter.y)
                        scale(
                            scaleX = shadowRadiusX,
                            scaleY = shadowRadiusY,
                            pivot = Offset.Zero,
                        )
                    }) {
                        drawCircle(
                            brush = shadow,
                            radius = 1F,
                            center = Offset.Zero,
                            alpha = (if (isDark) 0.30F else 0.23F) * (1F - lift * 0.4F),
                        )
                    }

                    // Stagger fixed paths: no random allocations or particle state per frame.
                    repeat(5) { index ->
                        val progress = (angle / (2 * PI).toFloat() + index / 5F) % 1F
                        val fade = sin(progress * PI).toFloat()
                        val side = if (index % 2 == 0) -1F else 1F
                        val originX = (7F + index % 3 * 2F) * side
                        val position = Offset(
                            x = center.x + (originX + side * progress * 5F + sin(progress * PI).toFloat()).dp.toPx(),
                            y = center.y + (5F - progress * (24F + index % 3 * 3F)).dp.toPx(),
                        )
                        val radius = (0.6F + index % 3 * 0.15F).dp.toPx()
                        val opacity = fade * fade * 0.42F
                        drawCircle(
                            color = particleColor,
                            radius = radius * 2.5F,
                            center = position,
                            alpha = opacity * 0.12F,
                        )
                        drawCircle(
                            color = particleColor,
                            radius = radius,
                            center = position,
                            alpha = opacity,
                        )
                    }
                }
            }
    ) {
        Image(
            painter = painterResource(ReviewReaction.BRILLIANT.drawable),
            contentDescription = stringResource(ReviewReaction.BRILLIANT.contentDescription),
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    val angle = phase.value
                    val lift = (1F - cos(angle)) / 2F
                    translationY = -(1F + lift * 4F).dp.toPx()
                    rotationZ = sin(angle) * 3F
                    scaleX = 1F + lift * 0.025F
                    scaleY = scaleX
                }
        )
    }
}
