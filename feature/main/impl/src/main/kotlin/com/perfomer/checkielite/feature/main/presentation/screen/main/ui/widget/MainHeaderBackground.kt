package com.perfomer.checkielite.feature.main.presentation.screen.main.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

@Composable
internal fun MainHeaderBackground(
    content: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = LocalCuiPalette.current.BackgroundAccentPrimary
    val softAccent = LocalCuiPalette.current.BackgroundAccentSecondary

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

                onDrawBehind {
                    drawRect(brush = leftGlow)
                    drawRect(brush = rightGlow)
                    drawCircle(
                        color = softAccent.copy(alpha = 0.65F),
                        radius = 96.dp.toPx(),
                        center = Offset(size.width * 0.6F, -48.dp.toPx()),
                        style = ringStroke,
                    )
                    drawCircle(
                        color = softAccent.copy(alpha = 0.4F),
                        radius = 56.dp.toPx(),
                        center = Offset(size.width + 12.dp.toPx(), size.height * 0.24F),
                    )
                }
            }
    )
}
