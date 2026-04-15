package com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.ui.state

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface ChangelogUiState {

    data object Loading : ChangelogUiState

    data class Content(
        val markdown: String,
    ) : ChangelogUiState

    data object Error : ChangelogUiState
}
