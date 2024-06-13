package com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core

import com.perfomer.checkielite.core.entity.CheckieTag

internal sealed interface MainCommand {

    data object LoadReviews : MainCommand

    data object LoadTags : MainCommand
}

internal sealed interface MainNavigationCommand : MainCommand {

    class OpenReviewDetails(val reviewId: String) : MainNavigationCommand

    class OpenSearch(val tag: CheckieTag? = null) : MainNavigationCommand

    data object OpenReviewCreation : MainNavigationCommand
}