package com.perfomer.checkielite.feature.appfunctions

import androidx.appfunctions.AppFunctionContext
import androidx.appfunctions.service.AppFunction
import com.perfomer.checkielite.core.data.repository.ReviewRepository
import com.perfomer.checkielite.feature.appfunctions.entity.TrackedReview
import com.perfomer.checkielite.feature.appfunctions.mapper.toTrackedReview
import com.perfomer.checkielite.feature.appfunctions.util.coerceRating
import com.perfomer.checkielite.feature.appfunctions.util.filterReviews
import kotlinx.coroutines.flow.first

/**
 * App Functions exposed by Checkie so an on-device assistant can track and read reviews.
 */
internal class CheckieAppFunctions(
    private val reviewRepository: ReviewRepository,
) {

    /**
     * Tracks (creates) a product review.
     *
     * @param appFunctionContext Execution context provided by the system.
     * @param productName The product name, e.g. "Cola".
     * @param productBrand The brand, e.g. "Darkside". Optional.
     * @param rating Score from 0 to 10.
     * @param comment Optional free-form comment.
     * @return The created review.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun trackReview(
        appFunctionContext: AppFunctionContext,
        productName: String,
        productBrand: String? = null,
        rating: Int,
        comment: String? = null,
    ): TrackedReview {
        require(productName.isNotBlank()) { "Product name must not be blank" }

        val coercedRating = coerceRating(rating)
        reviewRepository.createReview(
            productName = productName,
            productBrand = productBrand,
            price = null,
            rating = coercedRating,
            pictures = emptyList(),
            tagsIds = emptySet(),
            comment = comment,
            advantages = null,
            disadvantages = null,
        )

        // createReview() returns Unit, so we recover the persisted record by reading the newest
        // matching entry; a same-millisecond collision is acceptable for the assistant path.
        return reviewRepository.getReviews().first()
            .filter {
                it.productName == productName &&
                        it.productBrand == productBrand &&
                        it.rating == coercedRating
            }
            .maxByOrNull { it.creationDate }
            ?.toTrackedReview()
            ?: error("Review was created but could not be retrieved")
    }

    /**
     * Finds previously tracked reviews, optionally filtered by a query matching brand or name.
     *
     * @param appFunctionContext Execution context provided by the system.
     * @param query Optional text to match against brand or product name.
     * @return Matching reviews (empty if none).
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun findReviews(
        appFunctionContext: AppFunctionContext,
        query: String? = null,
    ): List<TrackedReview> {
        val reviews = reviewRepository.getReviews().first()
        return filterReviews(reviews, query).map { it.toTrackedReview() }
    }
}
