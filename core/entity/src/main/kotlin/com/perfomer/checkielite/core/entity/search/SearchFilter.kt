package com.perfomer.checkielite.core.entity.search

import com.perfomer.checkielite.core.entity.CheckieTag
import java.io.Serializable

data class SearchFilters(
    val tags: ArrayList<CheckieTag> = arrayListOf(),
    val ratingRange: RatingRange = RatingRange(),
) : Serializable {

    val isEmpty: Boolean = tags.isEmpty() && ratingRange == RatingRange.default
}

data class RatingRange(
    val min: Int = 0,
    val max: Int = 10,
) : Serializable {

    companion object {
        val default = RatingRange(min = 0, max = 10)
    }
}