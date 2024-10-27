package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.modifier.bottomStrokeOnScroll
import com.perfomer.checkielite.common.ui.cui.modifier.debounced
import com.perfomer.checkielite.common.ui.cui.widget.dropdown.CuiDropdownIcon
import com.perfomer.checkielite.common.ui.cui.widget.dropdown.CuiDropdownMenuItem
import com.perfomer.checkielite.common.ui.cui.widget.text.CuiFadedText
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview
import com.perfomer.checkielite.common.ui.util.StableInsets
import com.perfomer.checkielite.feature.reviewdetails.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ReviewDetailsAppBar(
    scrollState: LazyListState,
    title: String?,
    isMenuAvailable: Boolean,
    onNavigationIconClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shouldShowDivider by remember { derivedStateOf { scrollState.canScrollBackward } }

    TopAppBar(
        title = {
            val shouldShowTitle by remember { derivedStateOf { scrollState.firstVisibleItemIndex > 0 } }
            AnimatedVisibility(visible = shouldShowTitle && title != null, enter = fadeIn(tween(250)), exit = fadeOut(tween(250))) {
                CuiFadedText(
                    text = title!!,
                    fontSize = 18.sp,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        navigationIcon = {
            CuiToolbarNavigationIcon(
                painter = painterResource(CommonDrawable.ic_arrow_back),
                color = LocalCuiPalette.current.IconPrimary,
                onBackPress = onNavigationIconClick,
            )
        },
        actions = {
            AnimatedContent(
                targetState = isMenuAvailable,
                contentAlignment = Alignment.Center,
                transitionSpec = { (fadeIn()).togetherWith(fadeOut()) },
                label = "DropdownAnimatedContent",
            ) { shouldShowMenu ->
                if (shouldShowMenu) {
                    CuiDropdownIcon {
                        CuiDropdownMenuItem(
                            text = stringResource(R.string.reviewdetails_action_edit),
                            iconPainter = painterResource(CommonDrawable.ic_edit),
                            onClick = debounced(onEditClick),
                        )

                        CuiDropdownMenuItem(
                            text = stringResource(R.string.reviewdetails_action_delete),
                            iconPainter = painterResource(CommonDrawable.ic_delete),
                            iconTint = LocalCuiPalette.current.IconNegative,
                            onClick = debounced(onDeleteClick),
                        )
                    }
                } else {
                    CircularProgressIndicator(
                        color = LocalCuiPalette.current.IconPrimary,
                        strokeWidth = 2.dp,
                        modifier = Modifier
                            .size(20.dp)
                            .offset(x = (-18).dp)
                    )
                }
            }
        },
        windowInsets = StableInsets.statusBars(),
        modifier = modifier
            .fillMaxWidth()
            .bottomStrokeOnScroll(
                show = shouldShowDivider,
                strokeColor = LocalCuiPalette.current.OutlineSecondary,
            )
    )
}

@WidgetPreview
@Composable
private fun ReviewDetailsAppBarPreview() = CheckieLiteTheme {
    Box {
        ReviewDetailsAppBar(
            scrollState = rememberLazyListState(),
            title = null,
            isMenuAvailable = true,
            onNavigationIconClick = {},
            onDeleteClick = {},
            onEditClick = {},
        )
    }
}