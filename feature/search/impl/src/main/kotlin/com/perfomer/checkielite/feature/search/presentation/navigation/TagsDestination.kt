package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.Result
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class TagsDestination(
    val selectedTagsIds: @Contextual List<String>
) : Destination()

internal sealed interface TagsResult : Result {
    data class Success(val selectedTagsIds: List<String>) : TagsResult
}