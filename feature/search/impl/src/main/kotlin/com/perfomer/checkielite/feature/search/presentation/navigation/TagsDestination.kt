package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.navigation.DestinationWithResult
import com.perfomer.checkielite.core.navigation.Result
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
internal data class TagsDestination(
    val selectedTagsIds: @Contextual List<String>
) : DestinationWithResult<TagsResult>()

internal sealed interface TagsResult : Result {
    data class Success(val selectedTagsIds: List<String>) : TagsResult
}