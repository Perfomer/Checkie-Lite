package com.perfomer.checkielite.common.ui.cui.modifier

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.isRenderEffectSupported
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarCornerRadius
import com.perfomer.checkielite.common.ui.theme.LocalLiquidGlassEnabled

/** Follows the bottom edge of the toolbar, including both glass corners. */
fun Modifier.toolbarDivider(
    show: Boolean,
    strokeColor: Color,
): Modifier = composed {
    val alpha by animateFloatAsState(
        targetValue = if (show) 1F else 0F,
        animationSpec = tween(250),
        label = "ToolbarDividerAlpha",
    )
    val glassEnabled = LocalLiquidGlassEnabled.current && isRenderEffectSupported()

    drawWithCache {
        val strokeWidth = 1.dp.toPx()
        val inset = strokeWidth / 2F
        val left = inset
        val right = (size.width - inset).coerceAtLeast(left)
        val bottom = (size.height - inset).coerceAtLeast(inset)
        val radius = if (glassEnabled) {
            (CuiToolbarCornerRadius.toPx() - inset)
                .coerceIn(0F, minOf((right - left) / 2F, bottom - inset))
        } else {
            0F
        }
        val path = Path().apply {
            moveTo(left, bottom - radius)
            if (radius > 0F) {
                arcTo(Rect(left, bottom - 2F * radius, left + 2F * radius, bottom), 180F, -90F, false)
            }
            lineTo(right - radius, bottom)
            if (radius > 0F) {
                arcTo(Rect(right - 2F * radius, bottom - 2F * radius, right, bottom), 90F, -90F, false)
            }
        }

        onDrawWithContent {
            drawContent()
            drawPath(path = path, color = strokeColor, alpha = alpha, style = Stroke(strokeWidth))
        }
    }
}
