package com.perfomer.checkielite.feature.search.presentation.screen.tags.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.modifier.conditional
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiPrimaryButton
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiTagChip
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField2
import com.perfomer.checkielite.common.ui.cui.widget.scrim.verticalScrimBrush
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.pxToDp
import com.perfomer.checkielite.feature.search.R
import com.perfomer.checkielite.feature.search.presentation.screen.tags.ui.state.TagsUiState
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TagsScreen(
    state: TagsUiState,
    onTagClick: (tagId: String) -> Unit = {},
    onSearchQueryInput: (query: String) -> Unit = {},
    onSearchQueryClearClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    BoxWithConstraints {
        val maxHeight = remember(constraints.maxHeight) {
            constraints.maxHeight.pxToDp().dp * 0.7F
        }

        var shouldAnimateContentSize by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            delay(1_000L)
            shouldAnimateContentSize = true
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .requiredHeightIn(max = maxHeight)
                .conditional(shouldAnimateContentSize) { animateContentSize() }
        ) {
            Column {
                val scrollState = rememberScrollState()
                val shouldShowDivider by remember { derivedStateOf { scrollState.canScrollBackward } }

                Header(showDivider = shouldShowDivider)

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .navigationBarsPadding()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 120.dp, top = 88.dp)
                ) {
                    if (state.tags.isEmpty()) {
                        Text(
                            text = stringResource(CommonString.common_nothing_found),
                            color = LocalCuiPalette.current.TextSecondary,
                        )
                    }

                    for (tag in state.tags) {
                        key(tag.tagId) {
                            CuiTagChip(
                                text = tag.text,
                                emoji = tag.emoji,
                                isSelected = tag.isSelected,
                                onClick = { onTagClick(tag.tagId) },
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 24.dp)
                .padding(top = 56.dp)
        ) {
            SearchField(
                searchQuery = state.searchQuery,
                onSearchQueryInput = onSearchQueryInput,
                onSearchQueryClearClick = {
                    focusManager.clearFocus()
                    onSearchQueryClearClick()
                },
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .imePadding()
                .background(verticalScrimBrush())
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        ) {
            CuiPrimaryButton(
                text = stringResource(CommonString.common_done),
                onClick = onDoneClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun Header(
    showDivider: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.search_tags_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1F)
        )
    }

    AnimatedVisibility(visible = showDivider, enter = fadeIn(tween(250)), exit = fadeOut(tween(250))) {
        HorizontalDivider(
            color = LocalCuiPalette.current.OutlineSecondary,
        )
    }
}

@Composable
private fun SearchField(
    searchQuery: String,
    onSearchQueryInput: (query: String) -> Unit,
    onSearchQueryClearClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CuiOutlinedField2(
        value = searchQuery,
        onValueChange = onSearchQueryInput,
        placeholder = stringResource(CommonString.common_search),
        trailingIcon = {
            if (searchQuery.isBlank()) {
                Icon(
                    painter = painterResource(id = CommonDrawable.ic_search),
                    tint = LocalCuiPalette.current.IconSecondary,
                    contentDescription = null,
                    modifier = Modifier.offset(x = (-4).dp)
                )
            } else {
                CuiIconButton(
                    painter = painterResource(id = CommonDrawable.ic_cross),
                    contentDescription = stringResource(CommonString.common_clear),
                    tint = LocalCuiPalette.current.IconPrimary,
                    onClick = onSearchQueryClearClick,
                    modifier = Modifier.offset(x = (-4).dp)
                )
            }
        },
        modifier = modifier.background(
            color = LocalCuiPalette.current.BackgroundPrimary,
            shape = RoundedCornerShape(16.dp),
        )
    )
}

@ScreenPreview
@Composable
private fun TagsScreenPreview() = CheckieLiteTheme {
    TagsScreen(state = mockUiState)
}

internal val mockUiState = TagsUiState(
    tags = emptyList(),
    searchQuery = "",
)