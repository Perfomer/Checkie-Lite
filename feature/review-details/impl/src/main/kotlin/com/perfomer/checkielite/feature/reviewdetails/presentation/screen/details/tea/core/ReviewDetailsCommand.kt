package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core

import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationStartAction

internal sealed interface ReviewDetailsCommand {

    class LoadReview(val reviewId: String) : ReviewDetailsCommand

    class DeleteReview(val reviewId: String) : ReviewDetailsCommand
}

internal sealed interface ReviewDetailsNavigationCommand : ReviewDetailsCommand {

    data object Exit : ReviewDetailsNavigationCommand

    class OpenReviewEdit(
        val reviewId: String,
        val startAction: ReviewCreationStartAction,
    ) : ReviewDetailsNavigationCommand

    class OpenReviewDetails(val reviewId: String) : ReviewDetailsNavigationCommand

    class OpenSearch(val tagId: String) : ReviewDetailsNavigationCommand

    class OpenGallery(
        val picturesUri: List<String>,
        val currentPicturePosition: Int,
    ) : ReviewDetailsNavigationCommand
}