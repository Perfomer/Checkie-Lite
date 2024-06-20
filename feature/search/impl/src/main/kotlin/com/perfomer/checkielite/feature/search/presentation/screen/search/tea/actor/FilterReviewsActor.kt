package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor

import androidx.compose.ui.util.fastAll
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastFilter
import com.perfomer.checkielite.common.pure.search.smartFilterByQuery
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.search.RatingRange
import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy
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
            .filterByTags(command.filters.tagsIds)
            .filterByRatingRange(command.filters.ratingRange)
            .sortedBy(command.sorting)

        return ReviewsFiltered(filteredReviews)
    }

    private companion object {

        private fun List<CheckieReview>.filterByQuery(query: String): List<CheckieReview> {
            if (query.isEmpty()) return this

            return smartFilterByQuery(
                query = query,
                fieldProvider = {
                    buildList {
                        add(productName to 1F)
                        add(productBrand to 1F)
                        add(comment to 1.5F)
                        add(advantages to 1.5F)
                        add(disadvantages to 1.5F)
                        addAll(tags.map { tag -> tag.value to 1.5F })
                    }
                }
            )
        }

        private fun List<CheckieReview>.filterByTags(tagsIds: List<String>): List<CheckieReview> {
            if (tagsIds.isEmpty()) return this

            return fastFilter { review ->
                tagsIds.fastAll { filterTagId ->
                    review.tags.fastAny { reviewTag -> filterTagId == reviewTag.id }
                }
            }
        }

        private fun List<CheckieReview>.filterByRatingRange(ratingRange: RatingRange): List<CheckieReview> {
            if (ratingRange == RatingRange.default) return this

            val range = ratingRange.min..ratingRange.max
            return fastFilter { review -> review.rating in range }
        }

        private fun List<CheckieReview>.sortedBy(sorting: ReviewsSortingStrategy): List<CheckieReview> {
            return when (sorting) {
                ReviewsSortingStrategy.RELEVANCE -> this
                ReviewsSortingStrategy.NEWEST -> sortedByDescending { it.creationDate }
                ReviewsSortingStrategy.OLDEST -> sortedBy { it.creationDate }
                ReviewsSortingStrategy.MOST_RATED -> sortedByDescending { it.rating }
                ReviewsSortingStrategy.LEAST_RATED -> sortedBy { it.rating }
                ReviewsSortingStrategy.MOST_EXPENSIVE -> filter { it.price != null }.sortedByDescending { it.price!!.value }
                ReviewsSortingStrategy.CHEAPEST -> filter { it.price != null }.sortedBy { it.price!!.value }
            }
        }
    }
}