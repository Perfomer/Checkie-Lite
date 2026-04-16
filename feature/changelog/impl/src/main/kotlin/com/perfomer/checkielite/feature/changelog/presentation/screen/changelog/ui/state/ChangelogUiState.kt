package com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.ui.state

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.common.ui.cui.widget.state.ViewState

@Immutable
internal sealed interface ChangelogUiState : ViewState {

    data object Loading : ChangelogUiState, ViewState.Loading

    data class Content(
        val markdown: String,
    ) : ChangelogUiState, ViewState.Content

    data object Error : ChangelogUiState, ViewState.Error
}
