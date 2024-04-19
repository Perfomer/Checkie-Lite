package com.perfomer.checkielite.feature.search.presentation.screen.search.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.SearchUiState

@Composable
internal fun SearchScreen(
    state: SearchUiState,
    onNavigationIconClick: () -> Unit = {},
) {
}

@ScreenPreview
@Composable
private fun SearchScreenPreview() = CheckieLiteTheme {
    SearchScreen(state = mockUiState)
}

internal val mockUiState = SearchUiState