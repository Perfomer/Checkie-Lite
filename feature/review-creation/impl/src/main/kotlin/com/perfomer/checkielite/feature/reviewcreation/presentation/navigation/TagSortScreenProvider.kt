package com.perfomer.checkielite.feature.reviewcreation.presentation.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params
import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagSortingStrategy

internal class TagSortParams(val currentOption: TagSortingStrategy) : Params

internal sealed interface TagSortResult {
    data class Success(val option: TagSortingStrategy) : TagSortResult
}

internal fun interface TagSortScreenProvider {
    operator fun invoke(params: TagSortParams): CheckieScreen
}