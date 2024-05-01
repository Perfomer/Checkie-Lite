package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params

internal data class TagsParams(val selectedTags: ArrayList<CheckieTag>) : Params

internal sealed interface TagsResult {

    data class Success(val selectedTags: ArrayList<CheckieTag>) : TagsResult
}

internal fun interface TagsScreenProvider {

    operator fun invoke(params: TagsParams): CheckieScreen
}