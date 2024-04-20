package com.perfomer.checkielite.feature.reviewcreation.presentation.entity

import java.io.Serializable

internal sealed interface TagCreationMode : Serializable {

    data object Creation : TagCreationMode {
        private fun readResolve(): Any = Creation
    }

    data class Modification(
        val tagId: String,
    ) : TagCreationMode
}