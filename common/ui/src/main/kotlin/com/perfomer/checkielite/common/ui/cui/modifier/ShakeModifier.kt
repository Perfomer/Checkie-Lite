package com.perfomer.checkielite.common.ui.cui.modifier

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun rememberShakeController(): ShakeController {
    return remember { ShakeController() }
}

fun Modifier.shake(shakeController: ShakeController) = composed {
    val shakeConfig = shakeController.shakeConfig

    if (shakeConfig == null) {
        this
    } else {
        val shake = remember { Animatable(0F) }

        LaunchedEffect(shakeConfig) {
            for (i in 0..shakeConfig.iterations) {
                when (i % 2) {
                    0 -> shake.animateTo(1F, spring(stiffness = shakeConfig.intensity))
                    else -> shake.animateTo(-1F, spring(stiffness = shakeConfig.intensity))
                }
            }

            shake.animateTo(0F)

            shakeController.clear()
        }

        this
            .graphicsLayer {
                rotationX = shake.value * shakeConfig.rotateX
                rotationY = shake.value * shakeConfig.rotateY
            }
            .scale(
                scaleX = 1F + (shake.value * shakeConfig.scaleX),
                scaleY = 1F + (shake.value * shakeConfig.scaleY),
            )
            .offset {
                IntOffset(
                    (shake.value * shakeConfig.translateX).roundToInt(),
                    (shake.value * shakeConfig.translateY).roundToInt(),
                )
            }
    }
}

class ShakeController {

    var shakeConfig: ShakeConfig? by mutableStateOf(null)
        private set

    fun shake(shakeConfig: ShakeConfig) {
        this.shakeConfig = shakeConfig
    }

    fun clear() {
        this.shakeConfig = null
    }
}

data class ShakeConfig(
    val iterations: Int,
    val intensity: Float = 100_000F,
    val rotateX: Float = 0F,
    val rotateY: Float = 0F,
    val scaleX: Float = 0F,
    val scaleY: Float = 0F,
    val translateX: Float = 0F,
    val translateY: Float = 0F,
    val trigger: Long = System.currentTimeMillis(),
) {

    companion object {
        val inputError: ShakeConfig get() = ShakeConfig(iterations = 6, translateX = 8F)
    }
}