package com.perfomer.checkielite.feature.search.presentation.screen.tags.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiPrimaryButton
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.search.R
import com.perfomer.checkielite.feature.search.presentation.screen.tags.ui.state.TagsUiState

@Composable
internal fun TagsScreen(
    state: TagsUiState,
    onSortingOrderClick: () -> Unit = {},
    onTagClick: (tagId: String) -> Unit = {},
    onDoneClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.85F)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.navigationBarsPadding()
        ) {
            Header(
                onSearchClick = onSortingOrderClick,
                onSearchQueryTextInput = { },
                onSearchQueryClearClick = {},
            )

            Spacer(Modifier.height(16.dp))

            HorizontalDivider(
                color = LocalCuiPalette.current.OutlineSecondary,
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = state.tags,
                    key = { item -> item.tagId },
                ) { item ->
                    TagItem(
                        tag = item,
                        onClick = { onTagClick(item.tagId) },
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter)
        ) {
            CuiPrimaryButton(
                text = stringResource(CommonString.common_done),
                onClick = onDoneClick,
            )
        }
    }
}

@Composable
private fun Header(
    onSearchClick: () -> Unit,
    onSearchQueryTextInput: (text: String) -> Unit,
    onSearchQueryClearClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.search_tags_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1F)
        )

        CuiIconButton(painter = painterResource(CommonDrawable.ic_search), onClick = onSearchClick)
    }
}

@Composable
private fun TagItem(
    tag: TagsUiState.Tag,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        Text(
            text = tag.text,
            fontSize = 14.sp,
            fontWeight = if (tag.isSelected) FontWeight.Bold else FontWeight.Normal,
        )

        tag.emoji?.let { Text(it) }


        Spacer(Modifier.weight(1F))

        AnimatedVisibility(visible = tag.isSelected) {
            Icon(
                painter = painterResource(CommonDrawable.ic_tick),
                contentDescription = null,
                tint = LocalCuiPalette.current.IconPositive,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@ScreenPreview
@Composable
private fun TagsScreenPreview() = CheckieLiteTheme {
    TagsScreen(state = mockUiState)
}

internal val mockUiState = TagsUiState(emptyList())