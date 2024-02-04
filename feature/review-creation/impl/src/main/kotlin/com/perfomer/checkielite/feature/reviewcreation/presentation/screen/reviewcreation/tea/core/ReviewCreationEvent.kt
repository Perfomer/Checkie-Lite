package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core

internal sealed interface ReviewCreationEvent {

    data object Initialize : ReviewCreationEvent

    sealed interface ReviewCreation : ReviewCreationEvent {

        data object Succeed : ReviewCreation
    }
}

internal sealed interface ReviewCreationUiEvent : ReviewCreationEvent {

    data object OnPrimaryButtonClick : ReviewCreationUiEvent

    data object OnBackPress : ReviewCreationUiEvent

    sealed interface ProductInfo : ReviewCreationUiEvent {

        class OnProductNameTextInput(val text: String) : ProductInfo

        class OnBrandTextInput(val text: String) : ProductInfo

        data object OnAddPictureClick : ProductInfo

        class OnPictureDeleteClick(val pictureUri: String) : ProductInfo
    }

    sealed interface ReviewInfo : ReviewCreationUiEvent {

        class OnRatingSelect(val rating: Int) : ReviewInfo

        class OnReviewTextInput(val text: String) : ReviewInfo
    }
}