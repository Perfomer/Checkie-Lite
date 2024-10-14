package com.perfomer.checkielite.common.ui.cui.modifier

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.util.dpToPx

fun Modifier.shake(
    enabled: Boolean,
    amplitude: Dp = 4.dp,
    onAnimationFinish: () -> Unit,
) = composed(
    factory = {
        val amplitudePx = amplitude.value.toInt().dpToPx()

        val distance by animateFloatAsState(
            targetValue = if (enabled) amplitudePx else 0F,
            animationSpec = keyframes {
                durationMillis = 250

                for (i in 1..8) {
                    val x = when (i % 3) {
                        0 -> amplitudePx
                        1 -> -amplitudePx
                        else -> 0F
                    }

                    x at durationMillis / 10 * i using LinearEasing
                }
            },
            finishedListener = { onAnimationFinish.invoke() },
            label = "shake_anim",
        )

        Modifier.graphicsLayer {
            translationX = if (enabled) distance else 0F
        }
    },
    inspectorInfo = debugInspectorInfo {
        name = "shake"
        properties["enabled"] = enabled
    }
)