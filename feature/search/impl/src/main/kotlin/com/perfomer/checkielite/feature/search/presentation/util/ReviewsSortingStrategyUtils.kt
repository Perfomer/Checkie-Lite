package com.perfomer.checkielite.feature.search.presentation.util

import androidx.annotation.StringRes
import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.feature.search.R

@get:StringRes
internal val ReviewsSortingStrategy.label: Int
    get() = when (this) {
        ReviewsSortingStrategy.RELEVANCE -> R.string.search_sort_relevant
        ReviewsSortingStrategy.NEWEST -> R.string.search_sort_newest
        ReviewsSortingStrategy.OLDEST -> R.string.search_sort_oldest
        ReviewsSortingStrategy.MOST_RATED -> R.string.search_sort_most_rated
        ReviewsSortingStrategy.LEAST_RATED -> R.string.search_sort_least_rated
        ReviewsSortingStrategy.MOST_EXPENSIVE -> R.string.search_sort_most_expensive
        ReviewsSortingStrategy.CHEAPEST -> R.string.search_sort_cheapest
    }