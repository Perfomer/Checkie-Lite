package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.ui.state

import android.content.Context
import androidx.annotation.StringRes
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.entity.sort.TagSortingStrategy
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortState

internal class TagSortUiStateMapper(
    private val context: Context,
) : UiStateMapper<TagSortState, TagSortUiState> {

    override fun map(state: TagSortState): TagSortUiState {
        return TagSortUiState(
            items = state.sortingOptions.map { option ->
                option.toUi(isSelected = state.currentOption == option)
            },
        )
    }

    private fun TagSortingStrategy.toUi(isSelected: Boolean): SortingOption {
        return SortingOption(
            type = this,
            text = context.getString(label),
            isSelected = isSelected,
        )
    }

    private companion object {

        @get:StringRes
        private val TagSortingStrategy.label: Int
            get() = when (this) {
                TagSortingStrategy.USAGE_COUNT -> R.string.reviewcreation_tagsort_sort_usage_count
                TagSortingStrategy.ALPHABETICALLY -> R.string.reviewcreation_tagsort_sort_alphabetically
            }
    }
}