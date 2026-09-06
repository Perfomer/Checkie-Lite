package com.perfomer.checkielite.common.ui.cui.widget.toolbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.colorControls
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.isRenderEffectSupported
import com.perfomer.checkielite.common.ui.cui.modifier.thenElse
import com.perfomer.checkielite.common.ui.cui.modifier.thenIf
import com.perfomer.checkielite.common.ui.theme.LocalLiquidGlassEnabled

internal val CuiToolbarCornerRadius = 20.dp

private val GlassShape = RoundedCornerShape(
    bottomStart = CuiToolbarCornerRadius,
    bottomEnd = CuiToolbarCornerRadius,
)

@Composable
fun CuiGlassToolbarBackground(
    backdrop: Backdrop,
    backgroundColor: Color,
    progress: () -> Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            // Fade only the glass; toolbar text and touch targets stay fully opaque.
            .graphicsLayer { alpha = progress() }
            .thenIf(LocalLiquidGlassEnabled.current && isRenderEffectSupported()) {
                drawBackdrop(
                    backdrop = backdrop,
                    shape = { GlassShape },
                    effects = {
                        colorControls(saturation = 1.2F)
                        blur(radius = 4.dp.toPx())
                        // Backdrop skips refraction on Android 12, retaining the blur.
                        lens(
                            refractionHeight = 32.dp.toPx(),
                            refractionAmount = 18.dp.toPx(),
                            depthEffect = true,
                            chromaticAberration = true,
                        )
                    },
                    highlight = { Highlight.Default.copy(alpha = 0.55F) },
                    shadow = null,
                    onDrawSurface = { drawRect(backgroundColor.copy(alpha = 0.4F)) },
                )
            }
            .thenElse {
                // Keep the scroll background readable when glass is off or unsupported.
                drawBehind { drawRect(backgroundColor) }
            }
    )
}
