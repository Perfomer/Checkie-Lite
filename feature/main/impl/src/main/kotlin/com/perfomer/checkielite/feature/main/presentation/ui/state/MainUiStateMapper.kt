package com.perfomer.checkielite.feature.main.presentation.ui.state

import com.perfomer.checkielite.feature.main.presentation.tea.core.MainState
import com.perfomer.checkielite.tea.tea.component.UiStateMapper

internal class MainUiStateMapper : UiStateMapper<MainState, MainUiState> {

    override fun map(state: MainState): MainUiState {
        return MainUiState(
            searchQuery = state.searchQuery,
            reviews = emptyList(),
        )
    }

}