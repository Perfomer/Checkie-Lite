package com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state

import android.content.Context
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.common.ui.cui.widget.cell.ReviewItem
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.search.RatingRange
import com.perfomer.checkielite.core.entity.search.SearchSorting
import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.core.entity.sort.SortingOrder
import com.perfomer.checkielite.feature.search.R
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchState
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.Filter.FilterType
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.Filter.LeadingIcon
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.Filter.LeadingIconType
import kotlinx.collections.immutable.toPersistentList

internal class SearchUiStateMapper(
    private val context: Context,
) : UiStateMapper<SearchState, SearchUiState> {

    override fun map(state: SearchState): SearchUiState {
        return SearchUiState(
            searchQuery = state.searchQuery,
            filters = listOf(
                createTagFilter(state),
                createRatingFilter(state),
                createSortFilter(state),
            ).toPersistentList(),
            reviews = state.currentReviews
                .map { it.toUiItem() }
                .toPersistentList(),
            showRecentSearchesTitle = state.hasSearchConditions,
        )
    }

    private fun createSortFilter(state: SearchState): Filter {
        val sorting = state.searchSorting
        val isApplied = sorting != SearchSorting.default

        val strategyResource = when (sorting.strategy) {
            ReviewsSortingStrategy.CREATION_DATE -> when (sorting.order) {
                SortingOrder.ASCENDING -> R.string.search_sort_oldest
                SortingOrder.DESCENDING -> R.string.search_sort_newest
            }
            ReviewsSortingStrategy.RATING -> when (sorting.order) {
                SortingOrder.ASCENDING -> R.string.search_sort_least_rated
                SortingOrder.DESCENDING -> R.string.search_sort_most_rated
            }
        }

        val postfix = ": " + context.getString(strategyResource)

        return Filter(
            type = FilterType.SORT,
            text = context.getString(R.string.search_sort) + postfix,
            isApplied = isApplied,
        )
    }

    private fun createRatingFilter(state: SearchState): Filter {
        val ratingRange = state.searchFilters.ratingRange
        val isApplied = ratingRange != RatingRange.default
        val postfix = if (isApplied) ": ${ratingRange.min}-${ratingRange.max}" else ""

        return Filter(
            type = FilterType.RATING,
            text = context.getString(R.string.search_filter_rating) + postfix,
            isApplied = isApplied,
        )
    }

    private fun createTagFilter(state: SearchState): Filter {
        val tags = state.searchFilters.tags
        val firstTag by lazy { tags.first() }

        return Filter(
            type = FilterType.TAGS,
            leadingIcon = when (tags.size) {
                0 -> null
                1 -> firstTag.emoji?.let { LeadingIcon(it, LeadingIconType.EMOJI) }
                else -> LeadingIcon(tags.size.toString(), LeadingIconType.BADGE)
            },
            text = when (tags.size) {
                1 -> firstTag.value
                else -> context.getString(R.string.search_filters_tags)
            },
            isApplied = tags.size > 0,
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