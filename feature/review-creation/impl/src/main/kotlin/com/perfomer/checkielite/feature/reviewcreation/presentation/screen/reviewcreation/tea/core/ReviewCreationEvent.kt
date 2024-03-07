package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core

import com.perfomer.checkielite.core.entity.CheckieReview

internal sealed interface ReviewCreationEvent {

    data object Initialize : ReviewCreationEvent

    sealed interface ReviewLoading : ReviewCreationEvent {
        data object Started : ReviewLoading
        class Succeed(val review: CheckieReview) : ReviewLoading
        class Failed(val error: Throwable) : ReviewLoading
    }

    sealed interface ReviewSaving : ReviewCreationEvent {
        data object Started : ReviewSaving
        data object Succeed : ReviewSaving
        data object Failed : ReviewSaving
    }
}

internal sealed interface ReviewCreationUiEvent : ReviewCreationEvent {

    data object OnBackPress : ReviewCreationUiEvent

    data object OnPrimaryButtonClick : ReviewCreationUiEvent

    data object OnConfirmExitClick : ReviewCreationUiEvent

    sealed interface ProductInfo : ReviewCreationUiEvent {

        class OnProductNameTextInput(val text: String) : ProductInfo

        class OnBrandTextInput(val text: String) : ProductInfo

        data object OnAddPictureClick : ProductInfo

        class OnPictureClick(val position: Int) : ProductInfo

        class OnPictureDeleteClick(val position: Int) : ProductInfo

        class OnPictureReorder(val fromPosition: Int, val toPosition: Int) : ProductInfo
    }

    sealed interface ReviewInfo : ReviewCreationUiEvent {

        class OnRatingSelect(val rating: Int) : ReviewInfo

        class OnReviewTextInput(val text: String) : ReviewInfo
    }
}

internal sealed interface ReviewCreationNavigationEvent : ReviewCreationEvent {

    data class OnPhotoPick(val uri: String) : ReviewCreationNavigationEvent
}