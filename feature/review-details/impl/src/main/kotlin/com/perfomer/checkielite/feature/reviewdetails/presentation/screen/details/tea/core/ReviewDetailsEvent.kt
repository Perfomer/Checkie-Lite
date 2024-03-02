package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core

import com.perfomer.checkielite.core.entity.CheckieReview

internal sealed interface ReviewDetailsEvent {

    data object Initialize : ReviewDetailsEvent

    sealed interface ReviewLoading : ReviewDetailsEvent {

        class Succeed(val review: CheckieReview) : ReviewLoading
    }

    sealed interface ReviewDeletion : ReviewDetailsEvent {

        data object Succeed : ReviewDeletion
    }
}

internal sealed interface ReviewDetailsUiEvent : ReviewDetailsEvent {

    data object OnBackPress : ReviewDetailsUiEvent

    class OnPictureSelect(val position: Int) : ReviewDetailsUiEvent

    data object OnEmptyImageClick : ReviewDetailsUiEvent

    data object OnEmptyReviewTextClick : ReviewDetailsUiEvent

    data object OnEditClick : ReviewDetailsUiEvent

    data object OnDeleteClick : ReviewDetailsUiEvent

    data object OnConfirmDeleteClick : ReviewDetailsUiEvent
}