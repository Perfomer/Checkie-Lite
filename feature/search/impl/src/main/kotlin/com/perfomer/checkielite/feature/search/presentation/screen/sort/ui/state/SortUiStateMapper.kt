package com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.state

import android.content.Context
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortState

internal class SortUiStateMapper(
    private val context: Context,
) : UiStateMapper<SortState, SortUiState> {

    override fun map(state: SortState): SortUiState {
        return SortUiState
    }
}