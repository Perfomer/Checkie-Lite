package group.bakemate.feature.main.presentation.ui.state

import group.bakemate.feature.main.presentation.tea.core.MainState
import group.bakemate.tea.tea.component.UiStateMapper

internal class MainUiStateMapper : UiStateMapper<MainState, MainUiState> {

    override fun map(state: MainState): MainUiState {
        return MainUiState(
            searchQuery = state.searchQuery,
            reviews = emptyList()
        )
    }

}