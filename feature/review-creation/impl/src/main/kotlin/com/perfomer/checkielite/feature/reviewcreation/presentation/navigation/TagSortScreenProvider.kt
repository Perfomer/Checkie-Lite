package com.perfomer.checkielite.feature.reviewcreation.presentation.navigation

import com.perfomer.checkielite.core.entity.sort.TagSortingStrategy
import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params

internal class TagSortParams(val currentOption: TagSortingStrategy) : Params

internal sealed interface TagSortResult {
    data class Success(val option: TagSortingStrategy) : TagSortResult
}

internal fun interface TagSortScreenProvider {
    operator fun invoke(params: TagSortParams): CheckieScreen
}