package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.core.navigation.Destination
import kotlinx.serialization.Serializable

@Serializable
data class SortDestination(
    val sorting: ReviewsSortingStrategy,
) : Destination()

internal sealed interface SortResult {
    data class Success(val sorting: ReviewsSortingStrategy) : SortResult
}