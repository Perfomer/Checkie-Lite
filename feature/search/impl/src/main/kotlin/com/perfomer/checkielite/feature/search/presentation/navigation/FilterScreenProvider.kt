package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.entity.search.SearchFilters
import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params
import java.io.Serializable

internal data class FilterParams(
    val currentFilters: SearchFilters,
) : Params

internal sealed interface FilterResult : Serializable {

    data class Applied(
        val filters: SearchFilters,
    ) : FilterResult

    data object Reset : FilterResult {
        private fun readResolve(): Any = Reset
    }
}

internal fun interface FilterScreenProvider {

    operator fun invoke(params: FilterParams): CheckieScreen
}