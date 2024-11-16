package com.perfomer.checkielite.feature.reviewcreation.entity

import kotlinx.serialization.Serializable

@Serializable
sealed interface ReviewCreationMode {

    val startAction: ReviewCreationStartAction

    @Serializable
    data object Creation : ReviewCreationMode {
        override val startAction: ReviewCreationStartAction = ReviewCreationStartAction.NONE
    }

    @Serializable
    data class Modification(
        val reviewId: String,
        override val startAction: ReviewCreationStartAction = ReviewCreationStartAction.NONE,
    ) : ReviewCreationMode
}