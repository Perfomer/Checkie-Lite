package com.perfomer.checkielite.feature.appfunctions.util

internal const val MIN_RATING = 0
internal const val MAX_RATING = 10

internal fun coerceRating(rating: Int): Int = rating.coerceIn(MIN_RATING, MAX_RATING)
