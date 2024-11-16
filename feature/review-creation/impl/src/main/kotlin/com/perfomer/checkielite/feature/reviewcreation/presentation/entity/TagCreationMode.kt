package com.perfomer.checkielite.feature.reviewcreation.presentation.entity

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface TagCreationMode {

    @Serializable
    data class Creation(
        val initialTagValue: String = "",
    ) : TagCreationMode

    @Serializable
    data class Modification(
        val tagId: String,
    ) : TagCreationMode
}