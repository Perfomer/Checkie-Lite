package com.perfomer.checkielite.feature.reviewcreation.presentation.navigation

import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params
import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagCreationMode

internal class TagCreationParams(val mode: TagCreationMode) : Params

internal sealed interface TagCreationResult {

    data class Created(val tag: CheckieTag) : TagCreationResult

    data class Deleted(val tagId: String) : TagCreationResult
}

internal fun interface TagCreationScreenProvider {

    operator fun invoke(params: TagCreationParams): CheckieScreen
}