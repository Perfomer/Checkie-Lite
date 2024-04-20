package com.perfomer.checkielite.feature.search.presentation.screen.sort.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.state.SortUiState

@Composable
internal fun SortScreen(
    state: SortUiState,
    onNavigationIconClick: () -> Unit = {},
) {
}

@ScreenPreview
@Composable
private fun SearchScreenPreview() = CheckieLiteTheme {
    SortScreen(state = mockUiState)
}

internal val mockUiState = SortUiState