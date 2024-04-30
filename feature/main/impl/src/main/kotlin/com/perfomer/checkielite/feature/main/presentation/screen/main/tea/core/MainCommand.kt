package com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core

internal sealed interface MainCommand {

    data object LoadReviews : MainCommand
}

internal sealed interface MainNavigationCommand : MainCommand {

    class OpenReviewDetails(val reviewId: String) : MainNavigationCommand

    class OpenSearch() : MainNavigationCommand

    data object OpenReviewCreation : MainNavigationCommand
}