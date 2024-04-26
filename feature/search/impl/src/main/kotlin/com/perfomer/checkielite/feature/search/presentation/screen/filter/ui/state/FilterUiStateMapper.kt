package com.perfomer.checkielite.feature.search.presentation.screen.filter.ui.state

import android.content.Context
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterState

internal class FilterUiStateMapper(
    private val context: Context,
) : UiStateMapper<FilterState, FilterUiState> {

    override fun map(state: FilterState): FilterUiState {
        return FilterUiState()
    }
}