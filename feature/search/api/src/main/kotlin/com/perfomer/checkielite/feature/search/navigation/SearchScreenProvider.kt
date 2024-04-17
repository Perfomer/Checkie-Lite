package com.perfomer.checkielite.feature.search.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params

data class SearchParams(
    val tags: List<String>,
) : Params

fun interface SearchScreenProvider {

    operator fun invoke(params: SearchParams): CheckieScreen
}