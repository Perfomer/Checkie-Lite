package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor

import androidx.compose.ui.util.fastAll
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastFilter
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.core.entity.search.RatingRange
import com.perfomer.checkielite.core.entity.search.SearchSorting
import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.core.entity.sort.SortingOrder
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.FilterReviews
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.ReviewsFiltered
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
internal class FilterReviewsActor : Actor<SearchCommand, SearchEvent> {

    override fun act(commands: Flow<SearchCommand>): Flow<SearchEvent> {
        return commands.filterIsInstance<FilterReviews>()
            .mapLatest(::handleCommand)
    }

    private fun handleCommand(command: FilterReviews): SearchEvent {
        val filteredReviews = command.reviews
            .filterByQuery(command.query.trim())
            .filterByTags(command.filters.tags)
            .filterByRatingRange(command.filters.ratingRange)
            .sortedBy(command.sorting)

        return ReviewsFiltered(filteredReviews)
    }

    private companion object {

        private fun List<CheckieReview>.filterByQuery(query: String): List<CheckieReview> {
            fun String?.containsIgnoreCase(other: String): Boolean {
                return this?.contains(other, ignoreCase = false) ?: false
            }

            if (query.isEmpty()) return this

            return fastFilter { review ->
                review.productName.containsIgnoreCase(query) ||
                        review.productBrand.containsIgnoreCase(query) ||
                        review.comment.containsIgnoreCase(query) ||
                        review.advantages.containsIgnoreCase(query) ||
                        review.disadvantages.containsIgnoreCase(query) ||
                        review.tags.any { tag -> tag.value.containsIgnoreCase(query) }
            }
        }

        private fun List<CheckieReview>.filterByTags(tags: List<CheckieTag>): List<CheckieReview> {
            if (tags.isEmpty()) return this

            return fastFilter { review ->
                tags.fastAll { filterTag ->
                    review.tags.fastAny { reviewTag -> filterTag.id == reviewTag.id }
                }
            }
        }

        private fun List<CheckieReview>.filterByRatingRange(ratingRange: RatingRange): List<CheckieReview> {
            if (ratingRange == RatingRange.default) return this

            val range = ratingRange.min..ratingRange.max
            return fastFilter { review -> review.rating in range }
        }

        private fun List<CheckieReview>.sortedBy(sorting: SearchSorting): List<CheckieReview> {
            @Suppress("UNCHECKED_CAST")
            fun CheckieReview.getSortingValue(strategy: ReviewsSortingStrategy): Comparable<Any> {
                return when (strategy) {
                    ReviewsSortingStrategy.CREATION_DATE -> creationDate
                    ReviewsSortingStrategy.RATING -> rating
                } as Comparable<Any>
            }

            return when (sorting.order) {
                SortingOrder.ASCENDING -> sortedBy { it.getSortingValue(sorting.strategy) }
                SortingOrder.DESCENDING -> sortedByDescending { it.getSortingValue(sorting.strategy) }
            }
        }
    }
}