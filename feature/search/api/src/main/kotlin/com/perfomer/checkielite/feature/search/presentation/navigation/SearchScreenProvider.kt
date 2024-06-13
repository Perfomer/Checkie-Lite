package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params

data class SearchParams(
    val tagId: String?,
) : Params

fun interface SearchScreenProvider {

    operator fun invoke(params: SearchParams): CheckieScreen
}