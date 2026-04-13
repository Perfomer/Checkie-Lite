package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget

import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.widget.progress.CuiProgressBar
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.util.StableInsets
import com.perfomer.checkielite.feature.reviewcreation.R

@Composable
internal fun ProgressAppBar(
    pagerState: PagerState,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ProgressAppBar(
        step = pagerState.currentPage + 1,
        stepsCount = pagerState.pageCount,
        onBackPress = onBackPress,
        modifier = modifier,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ProgressAppBar(
    step: Int,
    stepsCount: Int,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isFirstStep = step == 1

    CenterAlignedTopAppBar(
        title = {
            CuiProgressBar(
                progress = step.toFloat() / stepsCount,
                modifier = Modifier.width(width = 160.dp),
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackPress,
                modifier = Modifier.padding(start = 8.dp),
            ) {
                Icon(
                    painter = rememberAnimatedVectorPainter(
                        animatedImageVector = AnimatedImageVector.animatedVectorResource(R.drawable.ic_back_close_animated),
                        atEnd = !isFirstStep,
                    ),
                    contentDescription = null,
                    tint = LocalCuiPalette.current.IconPrimary,
                )
            }
        },
        windowInsets = StableInsets.statusBars(),
        modifier = modifier,
    )
}
