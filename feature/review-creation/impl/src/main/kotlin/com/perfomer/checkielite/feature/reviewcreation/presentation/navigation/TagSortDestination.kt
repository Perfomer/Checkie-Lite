package com.perfomer.checkielite.feature.reviewcreation.presentation.navigation

import com.perfomer.checkielite.core.domain.entity.sort.TagSortingStrategy
import com.perfomer.checkielite.core.navigation.DestinationWithResult
import com.perfomer.checkielite.core.navigation.Result
import kotlinx.serialization.Serializable

@Serializable
internal data class TagSortDestination(
    val currentOption: TagSortingStrategy,
) : DestinationWithResult<TagSortResult>()

internal sealed interface TagSortResult : Result {
    data class Success(val option: TagSortingStrategy) : TagSortResult
}