package com.perfomer.checkielite.common.ui.cui.widget.rating

import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** A moving mesh with a circular fade, drawn independently of the rating's clipped ripple. */
@Composable
fun ReviewRatingHalo(
    centerFromStart: Dp,
    modifier: Modifier = Modifier
) {
    val isDark = LocalCuiPalette.current.BackgroundElevationBase.luminance() < 0.5F
    val layoutDirection = LocalLayoutDirection.current
    val transition = rememberInfiniteTransition(label = "Diamond halo")
    val phase = transition.animateFloat(
        initialValue = 0F,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3250, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Diamond halo mesh drift",
    )

    Box(
        modifier = modifier.drawWithCache {
            val center = Offset(
                x = if (layoutDirection == LayoutDirection.Ltr) centerFromStart.toPx() else size.width - centerFromStart.toPx(),
                y = size.height / 2F,
            )
            val radius = 40.dp.toPx()
            val bounds = Rect(center = center, radius = radius)
            val layerPaint = Paint()
            val fadeStops = Array(17) { index ->
                val position = index / 16F
                val progress = ((position - 0.2F) / 0.8F).coerceIn(0F, 1F)
                val alpha = 1F - progress * progress * (3F - 2F * progress)
                position to Color.Black.copy(alpha = alpha)
            }
            val fade = Brush.radialGradient(*fadeStops, center = center, radius = radius)
            val sky = meshBrush(Color(0xFF79CEFF), alpha = if (isDark) 0.22F else 0.48F)
            val azure = meshBrush(Color(0xFF397DF6), alpha = if (isDark) 0.40F else 0.66F)
            val periwinkle = meshBrush(Color(0xFF929BFF), alpha = if (isDark) 0.25F else 0.48F)
            val cyan = meshBrush(Color(0xFF41E3EB), alpha = if (isDark) 0.30F else 0.62F)
            val pearl = meshBrush(Color(0xFFE9FAFF), alpha = if (isDark) 0.06F else 0.88F)

            onDrawBehind {
                val driftX = sin(phase.value)
                val driftY = cos(phase.value)
                // The layer covers the full 80 dp halo, including the area outside the button.
                // Apply the mask only to the mesh, so it cannot erase the screen or the ripple.
                drawIntoCanvas { canvas ->
                    canvas.saveLayer(bounds, layerPaint)
                    clipRect(bounds.left, bounds.top, bounds.right, bounds.bottom) {
                        drawMeshSpot(sky, center, radius, radius)
                        drawMeshSpot(
                            brush = azure,
                            center = center + Offset(-0.28F + driftX * 0.18F, -0.28F + driftY * 0.14F) * radius,
                            radiusX = radius * 0.80F,
                            radiusY = radius * 0.72F,
                        )
                        drawMeshSpot(
                            brush = periwinkle,
                            center = center + Offset(0.34F + driftY * 0.14F, 0.08F + driftX * 0.18F) * radius,
                            radiusX = radius * 0.72F,
                            radiusY = radius * 0.86F,
                        )
                        drawMeshSpot(
                            brush = cyan,
                            center = center + Offset(-0.18F - driftY * 0.18F, 0.38F - driftX * 0.14F) * radius,
                            radiusX = radius * 0.76F,
                            radiusY = radius * 0.64F,
                        )
                        drawMeshSpot(
                            brush = pearl,
                            center = center + Offset(driftX * 0.10F, -0.06F + driftY * 0.08F) * radius,
                            radiusX = radius * 0.46F,
                            radiusY = radius * 0.40F,
                        )
                        drawRect(brush = fade, topLeft = bounds.topLeft, size = bounds.size, blendMode = BlendMode.DstIn)
                    }
                    canvas.restore()
                }
            }
        }
    )
}
