package com.perfomer.checkielite.core.domain.entity.search

import java.io.Serializable

data class SearchFilters(
    val tagsIds: List<String> = emptyList(),
    val ratingRange: RatingRange = RatingRange(),
) : Serializable {

    val isEmpty: Boolean = tagsIds.isEmpty() && ratingRange == RatingRange.default
}

data class RatingRange(
    val min: Int = 0,
    val max: Int = 10,
) : Serializable {

    companion object {
        val default = RatingRange(min = 0, max = 10)
    }
}