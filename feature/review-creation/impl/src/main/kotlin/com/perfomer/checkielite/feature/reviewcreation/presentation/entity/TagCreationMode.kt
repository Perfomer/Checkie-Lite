package com.perfomer.checkielite.feature.reviewcreation.presentation.entity

import java.io.Serializable

internal sealed interface TagCreationMode : Serializable {

    data class Creation(
        val initialTagValue: String = "",
    ) : TagCreationMode

    data class Modification(
        val tagId: String,
    ) : TagCreationMode
}