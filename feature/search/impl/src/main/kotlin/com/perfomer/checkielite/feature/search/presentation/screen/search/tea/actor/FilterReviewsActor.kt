package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor

import androidx.compose.ui.util.fastAll
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastFilter
import androidx.compose.ui.util.fastMap
import com.perfomer.checkielite.common.pure.search.DamerauLevenshteinWeights
import com.perfomer.checkielite.common.pure.search.score
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
import kotlin.math.roundToInt

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

        private val weights: DamerauLevenshteinWeights = DamerauLevenshteinWeights(
            deleteCost = 15,
            insertCost = 15,
            replaceCost = 10,
            swapCost = 10,
        )

        private fun List<CheckieReview>.filterByQuery(query: String): List<CheckieReview> {
            fun calculateScore(value: String?, factor: Float): Int {
                return if (value.isNullOrBlank()) Int.MAX_VALUE
                else (score(target = query, source = value, weights = weights) * factor).roundToInt()
            }

            if (query.isEmpty()) return this

            val maxScore = ((query.length / 2.5F).toInt() * 200).coerceIn(100, 300)

            return fastMap { review ->
                val score = buildList {
                    add(calculateScore(review.productName, 1F))
                    add(calculateScore(review.productBrand, 1F))
                    add(calculateScore(review.comment, 1.5F))
                    add(calculateScore(review.advantages, 1.5F))
                    add(calculateScore(review.disadvantages, 1.5F))
                    addAll(review.tags.map { tag -> calculateScore(tag.value, 1.5F) })
                }.min()

                review to score
            }
                .fastFilter { (_, score) -> score < maxScore }
                .sortedBy { (_, score) -> score }
                .fastMap { (review, _) -> review }
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
            }
        }
    }
}