package com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.ui.widget

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.widget.skeleton.CuiSkeleton
import com.perfomer.checkielite.common.ui.cui.widget.skeleton.CuiSkeletonColumn
import com.perfomer.checkielite.common.ui.cui.widget.skeleton.CuiSkeletonScope

@Composable
internal fun ChangelogSkeleton(
    modifier: Modifier = Modifier,
) {
    CuiSkeletonColumn(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        SkeletonLine(widthFraction = 0.16F, height = 28.dp)
        Spacer(modifier = Modifier.height(36.dp))

        SkeletonLine(widthFraction = 0.32F, height = 20.dp)
        Spacer(modifier = Modifier.height(42.dp))

        SkeletonLine(widthFraction = 0.55F, height = 14.dp)
        Spacer(modifier = Modifier.height(13.dp))
        SkeletonLine(widthFraction = 0.92F, height = 14.dp)
        Spacer(modifier = Modifier.height(13.dp))
        SkeletonLine(widthFraction = 0.7F, height = 14.dp)
        Spacer(modifier = Modifier.height(13.dp))
        SkeletonLine(widthFraction = 0.75F, height = 14.dp)

        Spacer(modifier = Modifier.height(40.dp))
        SkeletonLine(widthFraction = 0.32F, height = 20.dp)
        Spacer(modifier = Modifier.height(38.dp))

        SkeletonLine(widthFraction = 1F, height = 14.dp)
        Spacer(modifier = Modifier.height(14.dp))
        SkeletonLine(widthFraction = 1F, height = 14.dp)
        Spacer(modifier = Modifier.height(14.dp))
        SkeletonLine(widthFraction = 0.72F, height = 14.dp)
        Spacer(modifier = Modifier.height(14.dp))

        SkeletonLine(widthFraction = 0.7F, height = 14.dp)
        Spacer(modifier = Modifier.height(14.dp))
        SkeletonLine(widthFraction = 1F, height = 14.dp)
        Spacer(modifier = Modifier.height(14.dp))
        SkeletonLine(widthFraction = 1F, height = 14.dp)

        Spacer(modifier = Modifier.height(38.dp))
        SkeletonLine(widthFraction = 0.32F, height = 20.dp)
        Spacer(modifier = Modifier.height(38.dp))

        SkeletonLine(widthFraction = 1F, height = 14.dp)
        Spacer(modifier = Modifier.height(14.dp))
        SkeletonLine(widthFraction = 0.55F, height = 14.dp)
        Spacer(modifier = Modifier.height(14.dp))
        SkeletonLine(widthFraction = 1F, height = 14.dp)
        Spacer(modifier = Modifier.height(14.dp))
        SkeletonLine(widthFraction = 0.78F, height = 14.dp)

        Spacer(modifier = Modifier.height(48.dp))
        SkeletonLine(widthFraction = 0.16F, height = 28.dp)
        Spacer(modifier = Modifier.height(36.dp))

        SkeletonLine(widthFraction = 0.32F, height = 20.dp)
        Spacer(modifier = Modifier.height(42.dp))

        SkeletonLine(widthFraction = 0.55F, height = 14.dp)
        Spacer(modifier = Modifier.height(13.dp))
        SkeletonLine(widthFraction = 0.92F, height = 14.dp)
        Spacer(modifier = Modifier.height(13.dp))
        SkeletonLine(widthFraction = 0.7F, height = 14.dp)
        Spacer(modifier = Modifier.height(13.dp))
        SkeletonLine(widthFraction = 0.75F, height = 14.dp)
    }
}

@Composable
private fun CuiSkeletonScope.SkeletonLine(
    widthFraction: Float,
    height: Dp,
) {
    CuiSkeleton(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(height)
    )
}
