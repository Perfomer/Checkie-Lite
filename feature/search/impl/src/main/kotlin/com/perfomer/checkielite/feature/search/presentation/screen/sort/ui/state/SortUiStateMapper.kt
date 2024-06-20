package com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.state

import android.content.Context
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortState
import com.perfomer.checkielite.feature.search.presentation.util.label

internal class SortUiStateMapper(
    private val context: Context,
) : UiStateMapper<SortState, SortUiState> {

    override fun map(state: SortState): SortUiState {
        return SortUiState(
            items = state.sortingOptions.map { option ->
                option.toUi(isSelected = state.currentOption == option)
            },
        )
    }

    private fun ReviewsSortingStrategy.toUi(isSelected: Boolean): SortingOption {
        return SortingOption(
            type = this,
            text = context.getString(label),
            isSelected = isSelected,
        )
    }
}