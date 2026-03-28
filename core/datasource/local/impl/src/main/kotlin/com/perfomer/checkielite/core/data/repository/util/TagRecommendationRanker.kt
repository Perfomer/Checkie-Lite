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
        val reviewsWithAllSelectedTags = reviews.filter { review ->
            review.containsAllTags(selectedTagIds)
        }

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

        val exactSupportByTagId = reviewsWithAllSelectedTags.countSupportByTagId(
            excludedTagIds = selectedTagIds,
        )
        val coherenceContextReviews = reviews.resolveCoherenceContext(
            reviewsWithAllSelectedTags = reviewsWithAllSelectedTags,
            productBrand = productBrand,
        )
        val coherenceSupportByTagId = coherenceContextReviews.countSupportByTagId(
            excludedTagIds = selectedTagIds,
        )
        val coherencePairSupportByTagIds = coherenceContextReviews.countPairSupportByTagIds(
            excludedTagIds = selectedTagIds,
        )

        return scoreByTagId.keys
            .sortedWith(
                compareByDescending<String> { scoreByTagId.getValue(it) }
                    .thenByDescending { supportByTagId[it] ?: 0 }
                    .thenBy { tagById.getValue(it).value.lowercase() }
            )
            .filter { tagId ->
                selectedTagIds.size <= 1 || (exactSupportByTagId[tagId] ?: 0) > 0
            }
            .fold(mutableListOf<String>()) { acceptedTagIds, candidateTagId ->
                if (
                    acceptedTagIds.none { acceptedTagId ->
                        hasStrongContradiction(
                            firstTagId = acceptedTagId,
                            secondTagId = candidateTagId,
                            supportByTagId = coherenceSupportByTagId,
                            pairSupportByTagIds = coherencePairSupportByTagIds,
                        )
                    }
                ) {
                    acceptedTagIds += candidateTagId
                }

                acceptedTagIds
            }
            .take(maxCount)
            .map(tagById::getValue)
    }

    private companion object {
        private const val BRAND_WEIGHT = 1.25
        private const val MIN_SUPPORT_FOR_CONTRADICTION = 2
    }

    private fun List<CheckieReview>.resolveCoherenceContext(
        reviewsWithAllSelectedTags: List<CheckieReview>,
        productBrand: String,
    ): List<CheckieReview> {
        if (productBrand.isBlank()) return reviewsWithAllSelectedTags

        val reviewsWithSelectedTagsAndBrand = reviewsWithAllSelectedTags.filter { review ->
            review.productBrand == productBrand
        }
        if (reviewsWithSelectedTagsAndBrand.isNotEmpty()) return reviewsWithSelectedTagsAndBrand
        if (reviewsWithAllSelectedTags.isNotEmpty()) return reviewsWithAllSelectedTags

        return filter { review -> review.productBrand == productBrand }
    }

    private fun List<CheckieReview>.countSupportByTagId(
        excludedTagIds: Set<String>,
    ): Map<String, Int> {
        val supportByTagId = linkedMapOf<String, Int>()

        for (review in this) {
            for (tag in review.tags) {
                if (tag.id in excludedTagIds) continue
                supportByTagId[tag.id] = (supportByTagId[tag.id] ?: 0) + 1
            }
        }

        return supportByTagId
    }

    private fun List<CheckieReview>.countPairSupportByTagIds(
        excludedTagIds: Set<String>,
    ): Map<Pair<String, String>, Int> {
        val pairSupportByTagIds = linkedMapOf<Pair<String, String>, Int>()

        for (review in this) {
            val tagIds = review.tags
                .map(CheckieTag::id)
                .filterNot(excludedTagIds::contains)

            for (firstIndex in 0 until tagIds.lastIndex) {
                for (secondIndex in firstIndex + 1..tagIds.lastIndex) {
                    val pair = normalizedTagIdsPair(
                        firstTagId = tagIds[firstIndex],
                        secondTagId = tagIds[secondIndex],
                    )
                    pairSupportByTagIds[pair] = (pairSupportByTagIds[pair] ?: 0) + 1
                }
            }
        }

        return pairSupportByTagIds
    }

    private fun CheckieReview.containsAllTags(tagIds: Set<String>): Boolean {
        if (tagIds.isEmpty()) return false

        val reviewTagIds = tags.mapTo(mutableSetOf(), CheckieTag::id)
        return tagIds.all(reviewTagIds::contains)
    }

    private fun hasStrongContradiction(
        firstTagId: String,
        secondTagId: String,
        supportByTagId: Map<String, Int>,
        pairSupportByTagIds: Map<Pair<String, String>, Int>,
    ): Boolean {
        val firstTagSupport = supportByTagId[firstTagId] ?: return false
        val secondTagSupport = supportByTagId[secondTagId] ?: return false

        if (
            firstTagSupport < MIN_SUPPORT_FOR_CONTRADICTION ||
            secondTagSupport < MIN_SUPPORT_FOR_CONTRADICTION
        ) {
            return false
        }

        return (pairSupportByTagIds[normalizedTagIdsPair(firstTagId, secondTagId)] ?: 0) == 0
    }

    private fun normalizedTagIdsPair(
        firstTagId: String,
        secondTagId: String,
    ): Pair<String, String> {
        return if (firstTagId <= secondTagId) {
            firstTagId to secondTagId
        } else {
            secondTagId to firstTagId
        }
    }
}
