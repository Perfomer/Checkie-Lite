package com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.ui.widget

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

@Composable
internal fun ChangelogSkeleton(
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "ChangelogSkeleton")
    val alpha = transition.animateFloat(
        initialValue = 0.45F,
        targetValue = 1F,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "ChangelogSkeletonAlpha",
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        SkeletonLine(widthFraction = 0.48F, height = 28.dp, alpha = alpha.value)
        Spacer(modifier = Modifier.height(20.dp))
        SkeletonLine(widthFraction = 0.72F, height = 18.dp, alpha = alpha.value)
        Spacer(modifier = Modifier.height(28.dp))

        SkeletonLine(widthFraction = 0.35F, height = 20.dp, alpha = alpha.value)
        Spacer(modifier = Modifier.height(12.dp))
        SkeletonLine(widthFraction = 1F, height = 14.dp, alpha = alpha.value)
        Spacer(modifier = Modifier.height(8.dp))
        SkeletonLine(widthFraction = 0.92F, height = 14.dp, alpha = alpha.value)
        Spacer(modifier = Modifier.height(8.dp))
        SkeletonLine(widthFraction = 0.8F, height = 14.dp, alpha = alpha.value)
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SkeletonLine(
    widthFraction: Float,
    height: Dp,
    alpha: Float,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(height)
            .alpha(alpha)
            .background(
                color = LocalCuiPalette.current.BackgroundSecondary,
                shape = RoundedCornerShape(12.dp),
            )
    )
}
