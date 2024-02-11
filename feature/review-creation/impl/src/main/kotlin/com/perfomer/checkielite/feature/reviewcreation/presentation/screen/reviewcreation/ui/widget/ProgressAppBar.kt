package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.CuiProgressBar
import com.perfomer.checkielite.common.ui.cui.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.CuiColorToken

@Composable
@OptIn(ExperimentalFoundationApi::class)
internal fun ProgressAppBar(
    pagerState: PagerState,
    navigationIconPainter: Painter,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
    firstStepNavigationIconPainter: Painter = navigationIconPainter,
) {
    ProgressAppBar(
        step = pagerState.currentPage + 1,
        stepsCount = pagerState.pageCount,
        navigationIconPainter = navigationIconPainter,
        firstStepNavigationIconPainter = firstStepNavigationIconPainter,
        onBackPress = onBackPress,
        modifier = modifier
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ProgressAppBar(
    step: Int,
    stepsCount: Int,
    navigationIconPainter: Painter,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
    firstStepNavigationIconPainter: Painter = navigationIconPainter,
) {
    CenterAlignedTopAppBar(
        title = {
            CuiProgressBar(
                progress = step.toFloat() / stepsCount,
                modifier = Modifier.width(width = 160.dp)
            )
        },
        navigationIcon = {
            val iconPainter by remember(step) {
                derivedStateOf {
                    if (step == 1) firstStepNavigationIconPainter
                    else navigationIconPainter
                }
            }

            CuiToolbarNavigationIcon(
                painter = iconPainter,
                color = CuiColorToken.OrangeDark,
                onBackPress = onBackPress,
            )
        },
        modifier = modifier
    )
}