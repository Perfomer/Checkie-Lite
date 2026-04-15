package com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.ui.state

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogState

internal class ChangelogUiStateMapper : UiStateMapper<ChangelogState, ChangelogUiState> {

    override fun map(state: ChangelogState): ChangelogUiState {
        return when (state.changelog) {
            is Lce.Loading -> ChangelogUiState.Loading
            is Lce.Content -> ChangelogUiState.Content(markdown = state.changelog.content)
            is Lce.Error -> ChangelogUiState.Error
        }
    }
}
