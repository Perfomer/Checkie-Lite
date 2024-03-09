package com.perfomer.checkielite.feature.reviewcreation.entity

import java.io.Serializable

sealed interface ReviewCreationMode : Serializable {

    val initialPage: ReviewCreationPage

    data object Creation : ReviewCreationMode {
        override val initialPage: ReviewCreationPage = ReviewCreationPage.entries.first()
        private fun readResolve(): Any = Creation
    }

    data class Modification(
        val reviewId: String,
        override val initialPage: ReviewCreationPage = ReviewCreationPage.entries.first(),
    ) : ReviewCreationMode
}