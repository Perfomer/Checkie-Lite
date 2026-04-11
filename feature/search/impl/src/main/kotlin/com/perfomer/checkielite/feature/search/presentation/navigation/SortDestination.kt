package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.domain.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.core.navigation.DestinationWithResult
import com.perfomer.checkielite.core.navigation.Result
import kotlinx.serialization.Serializable

@Serializable
internal data class SortDestination(
    val sorting: ReviewsSortingStrategy,
) : DestinationWithResult<SortResult>()

internal sealed interface SortResult : Result {
    data class Success(val sorting: ReviewsSortingStrategy) : SortResult
}