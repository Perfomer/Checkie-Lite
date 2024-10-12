package com.perfomer.checkielite.feature.reviewcreation.entity

import java.io.Serializable

sealed interface ReviewCreationMode : Serializable {

    val startAction: ReviewCreationStartAction

    data object Creation : ReviewCreationMode {
        override val startAction: ReviewCreationStartAction = ReviewCreationStartAction.NONE
        private fun readResolve(): Any = Creation
    }

    data class Modification(
        val reviewId: String,
        override val startAction: ReviewCreationStartAction = ReviewCreationStartAction.NONE,
    ) : ReviewCreationMode
}