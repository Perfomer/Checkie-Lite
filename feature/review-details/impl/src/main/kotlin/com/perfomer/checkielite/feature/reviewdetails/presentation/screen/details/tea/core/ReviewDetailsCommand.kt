package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core

import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage

internal sealed interface ReviewDetailsCommand {

    class LoadReview(val reviewId: String) : ReviewDetailsCommand
}

internal sealed interface ReviewDetailsNavigationCommand : ReviewDetailsCommand {

    data object Exit : ReviewDetailsNavigationCommand

    class OpenReviewEdit(
        val reviewId: String,
        val initialPage: ReviewCreationPage,
    ) : ReviewDetailsNavigationCommand
}