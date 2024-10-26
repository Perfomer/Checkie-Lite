package com.perfomer.checkielite.common.ui.cui.widget.pager

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.modifier.indicatorOffsetForPage

@Composable
fun CuiHorizontalPagerIndicator(
    state: PagerState,
    selectedColor: Color,
    defaultColor: Color,
    modifier: Modifier = Modifier,
    selectedWidth: Dp = 20.dp,
    defaultWidth: Dp = 6.dp,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        repeat(state.pageCount) { i ->
            val offset = state.indicatorOffsetForPage(i)
            val widthDifference = selectedWidth - defaultWidth
            val additionalWidth = widthDifference * offset

            val targetColor = if (state.currentPage == i) selectedColor else defaultColor

            val animatedColor by animateColorAsState(
                targetValue = targetColor,
                animationSpec = tween(durationMillis = 350),
                label = "animatedIndicatorColor",
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .width(defaultWidth + additionalWidth)
                    .height(defaultWidth)
                    .background(color = animatedColor, shape = CircleShape)
            )
        }
    }
}