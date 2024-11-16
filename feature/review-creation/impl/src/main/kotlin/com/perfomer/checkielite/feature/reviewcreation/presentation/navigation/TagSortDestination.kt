package com.perfomer.checkielite.feature.reviewcreation.presentation.navigation

import com.perfomer.checkielite.core.entity.sort.TagSortingStrategy
import com.perfomer.checkielite.core.navigation.Destination
import kotlinx.serialization.Serializable

@Serializable
internal data class TagSortDestination(
    val currentOption: TagSortingStrategy,
) : Destination()

internal sealed interface TagSortResult {
    data class Success(val option: TagSortingStrategy) : TagSortResult
}