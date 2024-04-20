package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params

internal data class SortParams(
    val tags: List<String>,
) : Params

internal fun interface SortScreenProvider {

    operator fun invoke(params: SortParams): CheckieScreen
}