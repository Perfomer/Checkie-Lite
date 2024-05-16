package com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state

import android.content.Context
import com.perfomer.checkielite.common.pure.state.content
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.common.ui.cui.widget.cell.ReviewItem
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.search.RatingRange
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchState
import kotlinx.collections.immutable.toPersistentList

internal class SearchUiStateMapper(
    private val context: Context,
) : UiStateMapper<SearchState, SearchUiState> {

    override fun map(state: SearchState): SearchUiState {
        return SearchUiState(
            searchQuery = state.searchQuery,
            ratingFilter = state.searchFilters.ratingRange
                .takeIf { it != RatingRange.default }
                ?.let { range -> RatingFilter(range.min, range.max) },
            tagFilters = state.searchFilters.tags
                .map { tag -> TagFilter(tagId = tag.id, emoji = tag.emoji, value = tag.value) }
                .toPersistentList(),
            reviews = state.currentReviews.content.orEmpty()
                .map { it.toUiItem() }
                .toPersistentList(),
            showRecentSearchesTitle = state.hasSearchConditions,
        )
    }

    private fun CheckieReview.toUiItem(): ReviewItem {
        return ReviewItem(
            id = id,
            title = productName,
            brand = productBrand,
            imageUri = pictures.firstOrNull()?.uri,
            rating = rating,
            isSyncing = isSyncing,
        )
    }
}