package com.perfomer.checkielite.core.data.repository.util

import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.core.domain.entity.review.CheckieTag

internal class TagRecommendationRanker {

    fun rank(
        reviews: List<CheckieReview>,
        selectedTagIds: Set<String>,
        productBrand: String,
        maxCount: Int,
    ): List<CheckieTag> {
        if (selectedTagIds.isEmpty() && productBrand.isBlank()) return emptyList()

        val scoreByTagId = linkedMapOf<String, Double>()
        val supportByTagId = linkedMapOf<String, Int>()
        val tagById = linkedMapOf<String, CheckieTag>()

        if (selectedTagIds.isNotEmpty()) {
            val reviewsBySelectedTagId = selectedTagIds.associateWith { selectedTagId ->
                reviews.filter { review -> review.tags.any { tag -> tag.id == selectedTagId } }
            }

            for ((_, matchedReviews) in reviewsBySelectedTagId) {
                val matchedReviewsCount = matchedReviews.size
                if (matchedReviewsCount == 0) continue

                val candidateCounts = linkedMapOf<CheckieTag, Int>()
                for (review in matchedReviews) {
                    for (tag in review.tags) {
                        if (tag.id in selectedTagIds) continue
                        candidateCounts[tag] = (candidateCounts[tag] ?: 0) + 1
                    }
                }

                for ((tag, count) in candidateCounts) {
                    tagById[tag.id] = tag
                    scoreByTagId[tag.id] = (scoreByTagId[tag.id] ?: 0.0) + (count.toDouble() / matchedReviewsCount)
                    supportByTagId[tag.id] = (supportByTagId[tag.id] ?: 0) + count
                }
            }
        }

        if (productBrand.isNotBlank()) {
            val reviewsWithBrand = reviews.filter { it.productBrand == productBrand }
            val reviewsWithBrandCount = reviewsWithBrand.size

            if (reviewsWithBrandCount > 0) {
                val candidateCounts = linkedMapOf<CheckieTag, Int>()
                for (review in reviewsWithBrand) {
                    for (tag in review.tags) {
                        if (tag.id in selectedTagIds) continue
                        candidateCounts[tag] = (candidateCounts[tag] ?: 0) + 1
                    }
                }

                for ((tag, count) in candidateCounts) {
                    tagById[tag.id] = tag
                    scoreByTagId[tag.id] = (scoreByTagId[tag.id] ?: 0.0) + ((count.toDouble() / reviewsWithBrandCount) * BRAND_WEIGHT)
                    supportByTagId[tag.id] = (supportByTagId[tag.id] ?: 0) + count
                }
            }
        }

        return scoreByTagId.keys
            .sortedWith(
                compareByDescending<String> { scoreByTagId.getValue(it) }
                    .thenByDescending { supportByTagId[it] ?: 0 }
                    .thenBy { tagById.getValue(it).value.lowercase() }
            )
            .take(maxCount)
            .map(tagById::getValue)
    }

    private companion object {
        private const val BRAND_WEIGHT = 1.25
    }
}
