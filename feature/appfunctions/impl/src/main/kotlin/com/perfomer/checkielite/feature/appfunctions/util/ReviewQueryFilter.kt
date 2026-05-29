package com.perfomer.checkielite.feature.appfunctions.util

import com.perfomer.checkielite.core.domain.entity.review.CheckieReview

internal const val DEFAULT_REVIEWS_LIMIT = 20

internal fun filterReviews(
    reviews: List<CheckieReview>,
    query: String?,
    limit: Int = DEFAULT_REVIEWS_LIMIT,
): List<CheckieReview> {
    val normalized = query?.trim().orEmpty()
    val matched = if (normalized.isEmpty()) {
        reviews
    } else {
        reviews.filter { review ->
            review.productName.contains(normalized, ignoreCase = true) ||
                    review.productBrand?.contains(normalized, ignoreCase = true) == true
        }
    }

    return matched
        .sortedByDescending { it.creationDate }
        .take(limit)
}
