package com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state

import android.content.Context
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchState

internal class SearchUiStateMapper(
    private val context: Context,
) : UiStateMapper<SearchState, SearchUiState> {

    override fun map(state: SearchState): SearchUiState {
        return SearchUiState
    }
}