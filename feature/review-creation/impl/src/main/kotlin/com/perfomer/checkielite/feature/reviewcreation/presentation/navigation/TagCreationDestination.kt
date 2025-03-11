package com.perfomer.checkielite.feature.reviewcreation.presentation.navigation

import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.Result
import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagCreationMode
import kotlinx.serialization.Serializable

@Serializable
internal data class TagCreationDestination(
    val mode: TagCreationMode,
) : Destination()

internal sealed interface TagCreationResult : Result {
    data class Created(val tagId: String) : TagCreationResult
    data class Modified(val tagId: String) : TagCreationResult
    data class Deleted(val tagId: String) : TagCreationResult
}