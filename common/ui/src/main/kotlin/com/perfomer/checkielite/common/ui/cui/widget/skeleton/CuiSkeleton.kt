@file:SuppressLint("ModifierParameter")

package com.perfomer.checkielite.common.ui.cui.widget.skeleton

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

private const val SkeletonMinAlpha = 0.45F
private const val SkeletonMaxAlpha = 1F
private const val SkeletonAlphaOffsetStep = 0.12F

@Composable
fun CuiSkeletonScope.CuiSkeleton(
    shape: Shape = RoundedCornerShape(12.dp),
    modifier: Modifier = Modifier
) {
    val skeletonProgress = LocalCuiSkeletonProgress.current
    val skeletonIndex = rememberSkeletonIndex()
    val waveProgress = shiftWave(
        baseProgress = skeletonProgress.value,
        offsetIndex = skeletonIndex,
        offsetStep = SkeletonAlphaOffsetStep
    )
    val skeletonAlpha = SkeletonMinAlpha + ((SkeletonMaxAlpha - SkeletonMinAlpha) * waveProgress)

    Box(
        modifier = modifier
            .graphicsLayer { alpha = skeletonAlpha }
            .background(
                color = LocalCuiPalette.current.BackgroundSecondary,
                shape = shape,
            )
    )
}

private fun shiftWave(
    baseProgress: Float,
    offsetIndex: Int,
    offsetStep: Float,
): Float {
    val shiftedProgress = ((baseProgress - (offsetIndex * offsetStep)) % 1F + 1F) % 1F

    return if (shiftedProgress <= 0.5F) {
        shiftedProgress * 2F
    } else {
        (1F - shiftedProgress) * 2F
    }
}