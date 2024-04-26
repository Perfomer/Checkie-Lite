package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params

internal data class FilterParams(
    val stub: Int = 0,
) : Params

sealed interface FilterResult {

    data class Success(
        val stub: Int,
    ) : FilterResult
}

internal fun interface FilterScreenProvider {

    operator fun invoke(params: FilterParams): CheckieScreen
}