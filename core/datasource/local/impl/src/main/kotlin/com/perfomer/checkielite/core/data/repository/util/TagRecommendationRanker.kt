package com.perfomer.checkielite.core.data.repository.util

import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.core.domain.entity.review.CheckieTag
import kotlin.math.min

internal class TagRecommendationRanker {

    fun rank(
        reviews: List<CheckieReview>,
        selectedTagIds: Set<String>,
        productBrand: String,
        maxCount: Int,
    ): List<CheckieTag> {
        if (selectedTagIds.isEmpty() && productBrand.isBlank()) return emptyList()

        val tagById = reviews
            .asSequence()
            .flatMap { review -> review.tags.asSequence() }
            .associateBy(CheckieTag::id)

        val reviewsWithAllSelectedTags = reviews.filter { review ->
            review.containsAllTags(selectedTagIds)
        }
        val reviewsWithAnySelectedTags = reviews.filter { review ->
            review.containsAnyTags(selectedTagIds)
        }
        val reviewsWithBrand = reviews.filter { review ->
            productBrand.isNotBlank() && review.productBrand == productBrand
        }
        val reviewsWithBrandAndAllSelectedTags = reviewsWithAllSelectedTags.filter { review ->
            productBrand.isNotBlank() && review.productBrand == productBrand
        }
        val reviewsWithBrandAndAnySelectedTags = reviewsWithAnySelectedTags.filter { review ->
            productBrand.isNotBlank() && review.productBrand == productBrand
        }
        val reviewsBySelectedTagId = selectedTagIds.associateWith { selectedTagId ->
            reviews.filter { review -> review.containsTag(selectedTagId) }
        }

        val recommendationTiers = buildRecommendationTiers(
            tagById = tagById,
            selectedTagIds = selectedTagIds,
            reviewsWithAllSelectedTags = reviewsWithAllSelectedTags,
            reviewsWithBrandAndAllSelectedTags = reviewsWithBrandAndAllSelectedTags,
            reviewsBySelectedTagId = reviewsBySelectedTagId,
            reviewsWithBrand = reviewsWithBrand,
        )
        val coherenceContextReviews = resolveCoherenceContext(
            reviewsWithBrandAndAllSelectedTags = reviewsWithBrandAndAllSelectedTags,
            reviewsWithAllSelectedTags = reviewsWithAllSelectedTags,
            reviewsWithBrandAndAnySelectedTags = reviewsWithBrandAndAnySelectedTags,
            reviewsWithAnySelectedTags = reviewsWithAnySelectedTags,
            reviewsWithBrand = reviewsWithBrand,
        )
        val coherenceSupportByTagId = coherenceContextReviews.countSupportByTagId(
            excludedTagIds = selectedTagIds,
        )
        val coherencePairSupportByTagIds = coherenceContextReviews.countPairSupportByTagIds(
            excludedTagIds = selectedTagIds,
        )

        return recommendationTiers
            .asSequence()
            .flatMap { tier -> tier.asSequence() }
            .distinctBy(CandidateRecommendation::tagId)
            .fold(mutableListOf<CandidateRecommendation>()) { acceptedRecommendations, candidate ->
                if (acceptedRecommendations.size >= maxCount) return@fold acceptedRecommendations

                if (
                    acceptedRecommendations.none { acceptedRecommendation ->
                        hasStrongContradiction(
                            firstTagId = acceptedRecommendation.tagId,
                            secondTagId = candidate.tagId,
                            supportByTagId = coherenceSupportByTagId,
                            pairSupportByTagIds = coherencePairSupportByTagIds,
                        )
                    }
                ) {
                    acceptedRecommendations += candidate
                }

                acceptedRecommendations
            }
            .map { recommendation -> tagById.getValue(recommendation.tagId) }
    }

    private fun buildRecommendationTiers(
        tagById: Map<String, CheckieTag>,
        selectedTagIds: Set<String>,
        reviewsWithAllSelectedTags: List<CheckieReview>,
        reviewsWithBrandAndAllSelectedTags: List<CheckieReview>,
        reviewsBySelectedTagId: Map<String, List<CheckieReview>>,
        reviewsWithBrand: List<CheckieReview>,
    ): List<List<CandidateRecommendation>> {
        val recommendationTiers = mutableListOf<List<CandidateRecommendation>>()

        if (selectedTagIds.isNotEmpty() && reviewsWithBrandAndAllSelectedTags.isNotEmpty()) {
            recommendationTiers += collectContextTierCandidates(
                tagById = tagById,
                contextReviews = reviewsWithBrandAndAllSelectedTags,
                excludedTagIds = selectedTagIds,
                confidenceThreshold = STRICT_CONFIDENCE_THRESHOLD,
                matchedSignalsCount = selectedTagIds.size + 1,
            )
        }

        if (selectedTagIds.isNotEmpty()) {
            recommendationTiers += collectContextTierCandidates(
                tagById = tagById,
                contextReviews = reviewsWithAllSelectedTags,
                excludedTagIds = selectedTagIds,
                confidenceThreshold = STRICT_CONFIDENCE_THRESHOLD,
                matchedSignalsCount = selectedTagIds.size,
            )
            recommendationTiers += collectSelectedFallbackTierCandidates(
                tagById = tagById,
                selectedTagIds = selectedTagIds,
                reviewsBySelectedTagId = reviewsBySelectedTagId,
            )
        }

        if (reviewsWithBrand.isNotEmpty()) {
            recommendationTiers += collectContextTierCandidates(
                tagById = tagById,
                contextReviews = reviewsWithBrand,
                excludedTagIds = selectedTagIds,
                confidenceThreshold = FALLBACK_CONFIDENCE_THRESHOLD,
                matchedSignalsCount = 1,
            )
        }

        return recommendationTiers.filter { tier -> tier.isNotEmpty() }
    }

    private fun collectContextTierCandidates(
        tagById: Map<String, CheckieTag>,
        contextReviews: List<CheckieReview>,
        excludedTagIds: Set<String>,
        confidenceThreshold: Double,
        matchedSignalsCount: Int,
    ): List<CandidateRecommendation> {
        if (contextReviews.isEmpty()) return emptyList()

        val contextReviewsCount = contextReviews.size

        return contextReviews.countSupportByTagId(excludedTagIds)
            .mapNotNull { (tagId, support) ->
                val confidence = support.toDouble() / contextReviewsCount
                CandidateRecommendation(
                    tagId = tagId,
                    confidence = confidence,
                    support = support,
                    matchedSignalsCount = matchedSignalsCount,
                ).takeIf { confidence >= confidenceThreshold }
            }
            .sortedWith(candidateRecommendationComparator(tagById))
    }

    private fun collectSelectedFallbackTierCandidates(
        tagById: Map<String, CheckieTag>,
        selectedTagIds: Set<String>,
        reviewsBySelectedTagId: Map<String, List<CheckieReview>>,
    ): List<CandidateRecommendation> {
        if (selectedTagIds.isEmpty()) return emptyList()

        val requiredSelectedMatches = min(
            SELECTED_FALLBACK_REQUIRED_MATCHES,
            selectedTagIds.size,
        )
        val confidenceByCandidateTagId = linkedMapOf<String, MutableList<Double>>()
        val supportByCandidateTagId = linkedMapOf<String, Int>()

        for ((selectedTagId, matchedReviews) in reviewsBySelectedTagId) {
            val matchedReviewsCount = matchedReviews.size
            if (matchedReviewsCount == 0) continue

            val candidateSupportByTagId = matchedReviews.countSupportByTagId(
                excludedTagIds = setOf(selectedTagId),
            )

            for ((candidateTagId, support) in candidateSupportByTagId) {
                if (candidateTagId in selectedTagIds) continue

                confidenceByCandidateTagId.getOrPut(candidateTagId) { mutableListOf() } +=
                    support.toDouble() / matchedReviewsCount
                supportByCandidateTagId[candidateTagId] = (supportByCandidateTagId[candidateTagId] ?: 0) + support
            }
        }

        return confidenceByCandidateTagId.mapNotNull { (candidateTagId, confidences) ->
            if (confidences.size < requiredSelectedMatches) return@mapNotNull null

            val strongestConfidences = confidences.sortedDescending().take(requiredSelectedMatches)
            val weakestRequiredConfidence = strongestConfidences.last()

            CandidateRecommendation(
                tagId = candidateTagId,
                confidence = strongestConfidences.average(),
                support = supportByCandidateTagId.getValue(candidateTagId),
                matchedSignalsCount = confidences.size,
            ).takeIf { weakestRequiredConfidence >= FALLBACK_CONFIDENCE_THRESHOLD }
        }
            .sortedWith(candidateRecommendationComparator(tagById))
    }

    private fun candidateRecommendationComparator(
        tagById: Map<String, CheckieTag>,
    ): Comparator<CandidateRecommendation> {
        return compareByDescending<CandidateRecommendation> { recommendation -> recommendation.confidence }
            .thenByDescending { recommendation -> recommendation.matchedSignalsCount }
            .thenByDescending { recommendation -> recommendation.support }
            .thenBy { recommendation -> tagById.getValue(recommendation.tagId).value.lowercase() }
    }

    private fun resolveCoherenceContext(
        reviewsWithBrandAndAllSelectedTags: List<CheckieReview>,
        reviewsWithAllSelectedTags: List<CheckieReview>,
        reviewsWithBrandAndAnySelectedTags: List<CheckieReview>,
        reviewsWithAnySelectedTags: List<CheckieReview>,
        reviewsWithBrand: List<CheckieReview>,
    ): List<CheckieReview> = when {
        reviewsWithBrandAndAllSelectedTags.isNotEmpty() -> reviewsWithBrandAndAllSelectedTags
        reviewsWithAllSelectedTags.isNotEmpty() -> reviewsWithAllSelectedTags
        reviewsWithBrandAndAnySelectedTags.isNotEmpty() -> reviewsWithBrandAndAnySelectedTags
        reviewsWithAnySelectedTags.isNotEmpty() -> reviewsWithAnySelectedTags
        else -> reviewsWithBrand
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

    private fun CheckieReview.containsAnyTags(tagIds: Set<String>): Boolean {
        if (tagIds.isEmpty()) return false
        return tags.any { tag -> tag.id in tagIds }
    }

    private fun CheckieReview.containsTag(tagId: String): Boolean {
        return tags.any { tag -> tag.id == tagId }
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

    private companion object {
        private const val STRICT_CONFIDENCE_THRESHOLD = 0.5
        private const val FALLBACK_CONFIDENCE_THRESHOLD = 0.3333333333333333
        private const val SELECTED_FALLBACK_REQUIRED_MATCHES = 2
        private const val MIN_SUPPORT_FOR_CONTRADICTION = 2
    }
}

private data class CandidateRecommendation(
    val tagId: String,
    val confidence: Double,
    val support: Int,
    val matchedSignalsCount: Int,
)
