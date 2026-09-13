package com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core

import com.perfomer.checkielite.core.domain.entity.review.CheckieReview

internal sealed interface MainCommand {

    data object LoadReviews : MainCommand

    data object LoadTags : MainCommand

    data object CheckAppUpdatedRecently : MainCommand

    data object HideChangelogBanner : MainCommand
}

internal sealed interface MainNavigationCommand : MainCommand {

    data object OpenChangelog : MainNavigationCommand

    class OpenReviewDetails(
        val reviewId: String,
        val initialReview: CheckieReview?,
    ) : MainNavigationCommand

    class OpenSearch(val tagId: String? = null) : MainNavigationCommand

    data object OpenReviewCreation : MainNavigationCommand

    data object OpenSettings : MainNavigationCommand
}
