package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core

import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage

internal sealed interface ReviewDetailsCommand {

    class LoadReview(val reviewId: String) : ReviewDetailsCommand

    class DeleteReview(val reviewId: String) : ReviewDetailsCommand
}

internal sealed interface ReviewDetailsNavigationCommand : ReviewDetailsCommand {

    data object Exit : ReviewDetailsNavigationCommand

    class OpenReviewEdit(
        val reviewId: String,
        val initialPage: ReviewCreationPage,
    ) : ReviewDetailsNavigationCommand

    class OpenReviewDetails(val reviewId: String) : ReviewDetailsNavigationCommand

    class OpenSearch(val tag: CheckieTag) : ReviewDetailsNavigationCommand

    class OpenGallery(
        val picturesUri: List<String>,
        val currentPicturePosition: Int,
    ) : ReviewDetailsNavigationCommand
}