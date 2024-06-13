package com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state

import android.content.Context
import com.perfomer.checkielite.common.pure.state.content
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.common.ui.cui.widget.cell.ReviewItem
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.search.RatingRange
import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.feature.search.R
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchState
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.Filter.FilterType
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.Filter.LeadingIcon
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.Filter.LeadingIconType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

internal class SearchUiStateMapper(
    private val context: Context,
) : UiStateMapper<SearchState, SearchUiState> {

    override fun map(state: SearchState): SearchUiState {
        return SearchUiState(
            searchQuery = state.searchQuery,
            filters = createFilters(state),
            reviews = state.currentReviews
                .map { it.toUiItem() }
                .toPersistentList(),
            contentType = if (state.hasSearchConditions) SearchContentType.CURRENT_SEARCH else SearchContentType.RECENT_SEARCHES,
        )
    }

    private fun createFilters(state: SearchState): ImmutableList<Filter> {
        return listOfNotNull(
            createTagFilter(state),
            createRatingFilter(state),
            createSortFilter(state),
        ).toPersistentList()
    }

    private fun createSortFilter(state: SearchState): Filter {
        val sorting = state.sortingStrategy
        val isApplied = sorting != ReviewsSortingStrategy.RELEVANCE

        val strategyResource = when (sorting) {
            ReviewsSortingStrategy.RELEVANCE -> R.string.search_sort_relevant
            ReviewsSortingStrategy.NEWEST -> R.string.search_sort_newest
            ReviewsSortingStrategy.OLDEST -> R.string.search_sort_oldest
            ReviewsSortingStrategy.MOST_RATED -> R.string.search_sort_most_rated
            ReviewsSortingStrategy.LEAST_RATED -> R.string.search_sort_least_rated
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

    private fun createTagFilter(state: SearchState): Filter? {
        val tags = state.allTags.content ?: return null
        val selectedTagsIds = state.searchFilters.tagsIds
        val firstSelectedTag by lazy { tags.first { it.id == selectedTagsIds.first() } }

        return Filter(
            type = FilterType.TAGS,
            leadingIcon = when (selectedTagsIds.size) {
                0 -> null
                1 -> firstSelectedTag.emoji?.let { LeadingIcon(it, LeadingIconType.EMOJI) }
                else -> LeadingIcon(selectedTagsIds.size.toString(), LeadingIconType.BADGE)
            },
            text = when (selectedTagsIds.size) {
                1 -> firstSelectedTag.value
                else -> context.getString(R.string.search_filters_tags)
            },
            isApplied = selectedTagsIds.isNotEmpty(),
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