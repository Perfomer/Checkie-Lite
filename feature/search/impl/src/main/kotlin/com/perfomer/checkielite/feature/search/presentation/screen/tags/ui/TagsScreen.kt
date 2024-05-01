package com.perfomer.checkielite.feature.search.presentation.screen.tags.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.search.presentation.screen.tags.ui.state.TagsUiState

@Composable
internal fun TagsScreen(
    state: TagsUiState,
    onDoneClick: () -> Unit = {},
) {
}

@ScreenPreview
@Composable
private fun TagsScreenPreview() = CheckieLiteTheme {
    TagsScreen(state = mockUiState)
}

internal val mockUiState = TagsUiState