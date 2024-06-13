package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params

internal data class TagsParams(val selectedTagsIds: ArrayList<String>) : Params

internal sealed interface TagsResult {

    data class Success(val selectedTagsIds: ArrayList<String>) : TagsResult
}

internal fun interface TagsScreenProvider {

    operator fun invoke(params: TagsParams): CheckieScreen
}