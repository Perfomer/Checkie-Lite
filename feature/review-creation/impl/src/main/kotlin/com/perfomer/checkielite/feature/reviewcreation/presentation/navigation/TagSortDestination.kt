package com.perfomer.checkielite.feature.reviewcreation.presentation.navigation

import com.perfomer.checkielite.core.domain.entity.sort.TagSortingStrategy
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.Result
import kotlinx.serialization.Serializable

@Serializable
internal data class TagSortDestination(
    val currentOption: TagSortingStrategy,
) : Destination()

internal sealed interface TagSortResult : Result {
    data class Success(val option: TagSortingStrategy) : TagSortResult
}