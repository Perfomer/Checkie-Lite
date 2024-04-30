package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.entity.search.SearchSorting
import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params

internal data class SortParams(val sorting: SearchSorting) : Params

internal sealed interface SortResult {

    data class Success(val sorting: SearchSorting) : SortResult
}

internal fun interface SortScreenProvider {

    operator fun invoke(params: SortParams): CheckieScreen
}