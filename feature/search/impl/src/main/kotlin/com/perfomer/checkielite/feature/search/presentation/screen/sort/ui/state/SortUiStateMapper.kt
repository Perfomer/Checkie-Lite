package com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.state

import android.content.Context
import androidx.annotation.StringRes
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.feature.search.R
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortState

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

    private companion object {

        @get:StringRes
        private val ReviewsSortingStrategy.label: Int
            get() = when (this) {
                ReviewsSortingStrategy.RELEVANCE -> R.string.search_sort_relevant
                ReviewsSortingStrategy.NEWEST -> R.string.search_sort_newest
                ReviewsSortingStrategy.OLDEST -> R.string.search_sort_oldest
                ReviewsSortingStrategy.MOST_RATED -> R.string.search_sort_most_rated
                ReviewsSortingStrategy.LEAST_RATED -> R.string.search_sort_least_rated
            }
    }
}