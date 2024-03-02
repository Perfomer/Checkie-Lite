package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.dropdown.CuiDropdownIcon
import com.perfomer.checkielite.common.ui.cui.dropdown.CuiDropdownMenuItem
import com.perfomer.checkielite.common.ui.cui.modifier.debounced
import com.perfomer.checkielite.common.ui.cui.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.feature.reviewdetails.impl.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ReviewDetailsAppBar(
    onNavigationIconClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
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
        }
    )
}