package com.perfomer.checkielite.feature.main.presentation.screen.main.ui.widget

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun MainHeaderBackground(
    content: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = LocalCuiPalette.current.BackgroundAccentPrimary
    val softAccent = LocalCuiPalette.current.BackgroundAccentSecondary
    val shapeAccent = lerp(softAccent, accent, 0.3F)
    val transition = rememberInfiniteTransition(label = "Header shapes")
    val phase = transition.animateFloat(
        initialValue = 0F,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 11000, easing = LinearEasing),
        ),
        label = "Header drift",
    )

    Box(
        content = content,
        modifier = modifier
            .clipToBounds()
            .drawWithCache {
                val leftCenter = Offset(size.width * 0.08F, 0F)
                val rightCenter = Offset(size.width * 0.98F, size.height * 0.2F)
                val leftGlow = Brush.radialGradient(
                    colors = listOf(accent.copy(alpha = 0.18F), accent.copy(alpha = 0F)),
                    center = leftCenter,
                    radius = size.height * 0.95F,
                )
                val rightGlow = Brush.radialGradient(
                    colors = listOf(softAccent.copy(alpha = 0.85F), softAccent.copy(alpha = 0F)),
                    center = rightCenter,
                    radius = size.height * 0.75F,
                )
                val ringStroke = Stroke(width = 36.dp.toPx())
                val smallRingStroke = Stroke(width = 5.dp.toPx())
                val squareSize = Size(48.dp.toPx(), 48.dp.toPx())
                val squareCorner = CornerRadius(12.dp.toPx())
                val pentagonRadius = 26.dp.toPx()
                val pentagon = Path().apply {
                    repeat(5) { index ->
                        val vertexAngle = (index * 2 * PI / 5 - PI / 2).toFloat()
                        val x = cos(vertexAngle) * pentagonRadius
                        val y = sin(vertexAngle) * pentagonRadius
                        if (index == 0) moveTo(x, y) else lineTo(x, y)
                    }
                    close()
                }
                val drift = 5.dp.toPx()
                // Anchor decorations above search, independently of the number of tag rows.
                val squareOrigin = Offset(size.width * 0.1F, 48.dp.toPx())
                val pentagonOrigin = Offset(size.width * 0.81F, 50.dp.toPx())
                val smallRingOrigin = Offset(size.width * 0.24F, 22.dp.toPx())

                onDrawBehind {
                    // Read animation state only while drawing, keeping layout and composition still.
                    val angle = phase.value
                    val ringOffset = Offset(sin(angle) * drift, cos(angle) * drift * 0.6F)
                    val squareOffset = Offset(cos(angle + 1F) * drift, sin(angle + 1F) * drift)
                    val pentagonOffset = Offset(sin(angle + 2F) * drift, cos(angle + 2F) * drift * 0.8F)
                    drawRect(brush = leftGlow)
                    drawRect(brush = rightGlow)
                    drawCircle(
                        color = softAccent.copy(alpha = 0.65F),
                        radius = 96.dp.toPx(),
                        center = Offset(size.width * 0.6F, -48.dp.toPx()) + ringOffset,
                        style = ringStroke,
                    )
                    drawCircle(
                        color = shapeAccent.copy(alpha = 0.4F),
                        radius = 56.dp.toPx(),
                        center = Offset(size.width + 12.dp.toPx(), 24.dp.toPx()) - squareOffset,
                    )

                    val squareCenter = squareOrigin + squareOffset
                    rotate(degrees = -18F, pivot = squareCenter) {
                        drawRoundRect(
                            color = shapeAccent.copy(alpha = 0.65F),
                            topLeft = squareCenter - Offset(squareSize.width / 2F, squareSize.height / 2F),
                            size = squareSize,
                            cornerRadius = squareCorner,
                        )
                    }

                    val pentagonCenter = pentagonOrigin + pentagonOffset
                    translate(left = pentagonCenter.x, top = pentagonCenter.y) {
                        drawPath(
                            path = pentagon,
                            color = shapeAccent.copy(alpha = 0.6F),
                        )
                    }
                    drawCircle(
                        color = shapeAccent.copy(alpha = 0.75F),
                        radius = 16.dp.toPx(),
                        center = smallRingOrigin - pentagonOffset,
                        style = smallRingStroke,
                    )
                }
            }
    )
}
