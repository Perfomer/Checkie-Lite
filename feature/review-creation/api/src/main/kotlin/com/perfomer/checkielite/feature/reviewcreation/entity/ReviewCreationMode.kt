package com.perfomer.checkielite.feature.reviewcreation.entity

sealed interface ReviewCreationMode {

    val initialPage: ReviewCreationPage

    data object Creation : ReviewCreationMode {
        override val initialPage: ReviewCreationPage = ReviewCreationPage.PRODUCT_INFO
    }

    data class Modification(
        val reviewId: String,
        override val initialPage: ReviewCreationPage = ReviewCreationPage.PRODUCT_INFO,
    ) : ReviewCreationMode
}