package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core

import com.perfomer.checkielite.core.entity.CheckieReview

internal sealed interface ReviewCreationCommand {

    class CreateReview(val review: CheckieReview) : ReviewCreationCommand
}

internal sealed interface ReviewCreationNavigationCommand : ReviewCreationCommand {

    data object Exit : ReviewCreationNavigationCommand
}