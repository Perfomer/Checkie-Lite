package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core

import com.perfomer.checkielite.core.entity.CheckieReview

internal sealed interface ReviewCreationEvent {

    data object Initialize : ReviewCreationEvent

    sealed interface ReviewCreation : ReviewCreationEvent {

        data object Succeed : ReviewCreation
    }
}

internal sealed interface ReviewCreationUiEvent : ReviewCreationEvent {

    data object OnPrimaryButtonClick : ReviewCreationUiEvent

    data object OnBackPress : ReviewCreationUiEvent
}