package com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.state

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.core.entity.sort.SortingOrder
import com.perfomer.checkielite.feature.search.R
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortState

internal class SortUiStateMapper(
    private val context: Context,
) : UiStateMapper<SortState, SortUiState> {

    override fun map(state: SortState): SortUiState {
        return SortUiState(
            isAscending = state.currentOrder == SortingOrder.ASCENDING,
            items = state.sortingOptions.map { option ->
                option.toUi(isSelected = state.currentOption == option)
            },
        )
    }

    private fun ReviewsSortingStrategy.toUi(isSelected: Boolean) : SortingOption {
        return SortingOption(
            type = this,
            text = context.getString(label),
            icon = icon,
            isSelected = isSelected,
        )
    }

    private companion object {

        @get:DrawableRes
        private val ReviewsSortingStrategy.icon: Int
            get() = when (this) {
                ReviewsSortingStrategy.CREATION_DATE -> R.drawable.search_ic_sort_creationdate
                ReviewsSortingStrategy.MODIFICATION_DATE -> R.drawable.search_ic_sort_modificationdate
                ReviewsSortingStrategy.RATING -> R.drawable.search_ic_sort_rating
                ReviewsSortingStrategy.NAME -> R.drawable.search_ic_sort_name
            }

        @get:StringRes
        private val ReviewsSortingStrategy.label: Int
            get() = when (this) {
                ReviewsSortingStrategy.CREATION_DATE -> R.string.search_sort_creationdate
                ReviewsSortingStrategy.MODIFICATION_DATE -> R.string.search_sort_modificationdate
                ReviewsSortingStrategy.RATING -> R.string.search_sort_rating
                ReviewsSortingStrategy.NAME -> R.string.search_sort_name
            }
    }
}