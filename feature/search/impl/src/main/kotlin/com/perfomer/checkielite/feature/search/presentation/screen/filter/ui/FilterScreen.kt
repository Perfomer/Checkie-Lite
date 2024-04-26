package com.perfomer.checkielite.feature.search.presentation.screen.filter.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.search.presentation.screen.filter.ui.state.FilterUiState

@Composable
internal fun FilterScreen(
    state: FilterUiState,
    onDoneClick: () -> Unit = {},
) {

}


@ScreenPreview
@Composable
private fun SearchScreenPreview() = CheckieLiteTheme {
    FilterScreen(state = mockUiState)
}

internal val mockUiState = FilterUiState()