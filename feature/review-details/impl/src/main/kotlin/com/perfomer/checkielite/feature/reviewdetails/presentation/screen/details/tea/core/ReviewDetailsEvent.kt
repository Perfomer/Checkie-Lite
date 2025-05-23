package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core

internal sealed interface ReviewDetailsEvent {

    sealed interface ReviewLoading : ReviewDetailsEvent {

        data object Started : ReviewLoading
        class Succeed(val details: ReviewDetails) : ReviewLoading
        class Failed(val error: Throwable) : ReviewLoading
    }

    sealed interface ReviewDeletion : ReviewDetailsEvent {

        data object Started : ReviewDeletion
        data object Succeed : ReviewDeletion
        data object Failed : ReviewDeletion
    }
}

internal sealed interface ReviewDetailsUiEvent : ReviewDetailsEvent {

    data object OnStart : ReviewDetailsUiEvent

    data object OnBackPress : ReviewDetailsUiEvent

    class OnPictureSelect(val position: Int) : ReviewDetailsUiEvent

    data object OnPictureClick : ReviewDetailsUiEvent

    data object OnEmptyImageClick : ReviewDetailsUiEvent

    data object OnEmptyPriceClick : ReviewDetailsUiEvent

    data object OnEmptyReviewTextClick : ReviewDetailsUiEvent

    data object OnEditClick : ReviewDetailsUiEvent

    data object OnDeleteClick : ReviewDetailsUiEvent

    data object OnConfirmDeleteClick : ReviewDetailsUiEvent

    data object OnAddTagsClick : ReviewDetailsUiEvent

    class OnTagClick(val tagId: String) : ReviewDetailsUiEvent

    class OnRecommendationClick(val recommendedReviewId: String) : ReviewDetailsUiEvent
}