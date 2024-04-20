package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.data.datasource.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.core.data.datasource.sort.SortingOrder
import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params

internal data class SortParams(
    val order: SortingOrder,
    val strategy: ReviewsSortingStrategy,
) : Params

sealed interface SortResult {

    data class Success(
        val order: SortingOrder,
        val strategy: ReviewsSortingStrategy,
    ) : SortResult
}

internal fun interface SortScreenProvider {

    operator fun invoke(params: SortParams): CheckieScreen
}