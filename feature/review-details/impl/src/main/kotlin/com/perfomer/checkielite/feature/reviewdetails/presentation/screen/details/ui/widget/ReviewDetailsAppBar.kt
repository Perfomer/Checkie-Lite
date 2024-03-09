package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.modifier.bottomStrokeOnScroll
import com.perfomer.checkielite.common.ui.cui.modifier.debounced
import com.perfomer.checkielite.common.ui.cui.widget.dropdown.CuiDropdownIcon
import com.perfomer.checkielite.common.ui.cui.widget.dropdown.CuiDropdownMenuItem
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.feature.reviewdetails.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ReviewDetailsAppBar(
    scrollState: ScrollableState,
    isMenuAvailable: Boolean,
    onNavigationIconClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val shouldShowDivider by remember { derivedStateOf { scrollState.canScrollBackward } }

    TopAppBar(
        title = {},
        navigationIcon = {
            CuiToolbarNavigationIcon(
                painter = painterResource(CommonDrawable.ic_arrow_back),
                color = LocalCuiPalette.current.IconPrimary,
                onBackPress = onNavigationIconClick,
            )
        },
        actions = {
            AnimatedVisibility(visible = isMenuAvailable, enter = fadeIn(), exit = fadeOut()) {
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
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .bottomStrokeOnScroll(
                show = shouldShowDivider,
                strokeColor = LocalCuiPalette.current.OutlineSecondary,
            )
    )
}