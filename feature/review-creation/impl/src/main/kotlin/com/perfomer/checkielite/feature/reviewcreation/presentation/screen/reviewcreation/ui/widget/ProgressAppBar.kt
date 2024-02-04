package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.CuiProgressBar
import com.perfomer.checkielite.common.ui.cui.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.CuiColorToken

@Composable
internal fun ProgressAppBar(
    step: Int,
    stepsCount: Int,
    trailingIconPainter: Painter,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppBarBox(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            CuiToolbarNavigationIcon(
                painter = trailingIconPainter,
                color = CuiColorToken.OrangeDark,
                onBackPress = onBackPress,
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1F)
            ) {
                CuiProgressBar(
                    progress = step.toFloat() / stepsCount,
                    modifier = Modifier.width(width = 160.dp)
                )
            }

            Spacer(modifier = Modifier.width(40.dp))
        }
    }
}