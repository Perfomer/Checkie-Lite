package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.modifier.debounced
import com.perfomer.checkielite.common.ui.cui.widget.dropdown.CuiDropdownIcon
import com.perfomer.checkielite.common.ui.cui.widget.dropdown.CuiDropdownMenuItem
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.feature.reviewdetails.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ReviewDetailsAppBar(
    scrollState: ScrollState,
    onNavigationIconClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Box {
        TopAppBar(
            title = {},
            navigationIcon = {
                CuiToolbarNavigationIcon(
                    painter = painterResource(CommonDrawable.ic_arrow_back),
                    color = CuiPalette.Light.IconPrimary,
                    onBackPress = onNavigationIconClick,
                )
            },
            actions = {
                CuiDropdownIcon {
                    CuiDropdownMenuItem(
                        text = stringResource(R.string.reviewdetails_action_edit),
                        iconPainter = painterResource(CommonDrawable.ic_pencil),
                        onClick = debounced(onEditClick),
                    )

                    CuiDropdownMenuItem(
                        text = stringResource(R.string.reviewdetails_action_delete),
                        iconPainter = painterResource(CommonDrawable.ic_delete),
                        onClick = debounced(onDeleteClick),
                    )
                }
            },
        )

        AnimatedVisibility(
            visible = scrollState.canScrollBackward, enter = fadeIn(tween(250)), exit = fadeOut(tween(250)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(CuiPalette.Light.OutlineSecondary)
            )
        }
    }
}