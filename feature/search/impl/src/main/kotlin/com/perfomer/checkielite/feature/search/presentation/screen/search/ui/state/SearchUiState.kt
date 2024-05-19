package com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.common.ui.cui.widget.cell.ReviewItem
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class SearchUiState(
    val searchQuery: String,
    val filters: ImmutableList<Filter>,
    val reviews: ImmutableList<ReviewItem>,
    val showRecentSearchesTitle: Boolean,
)

@Immutable
internal data class Filter(
    val type: FilterType,
    val text: String,
    val isApplied: Boolean,
    val leadingIcon: LeadingIcon? = null,
) {

    @Immutable
    data class LeadingIcon(
        val value: String,
        val type: LeadingIconType,
    )

    enum class LeadingIconType {
        BADGE, EMOJI
    }

    enum class FilterType {
        TAGS, RATING, SORT
    }
}