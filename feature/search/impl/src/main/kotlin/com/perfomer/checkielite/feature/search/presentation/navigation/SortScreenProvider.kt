package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params

internal data class SortParams(val sorting: ReviewsSortingStrategy) : Params

internal sealed interface SortResult {

    data class Success(val sorting: ReviewsSortingStrategy) : SortResult
}

internal fun interface SortScreenProvider {

    operator fun invoke(params: SortParams): CheckieScreen
}