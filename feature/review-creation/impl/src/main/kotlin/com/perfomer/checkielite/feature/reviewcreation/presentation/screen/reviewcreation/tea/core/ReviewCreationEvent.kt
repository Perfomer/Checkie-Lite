package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core

import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.core.entity.price.CheckieCurrency

internal sealed interface ReviewCreationEvent {

    data object Initialize : ReviewCreationEvent

    class BrandsSearchComplete(val brands: List<String>) : ReviewCreationEvent

    sealed interface ReviewLoading : ReviewCreationEvent {
        data object Started : ReviewLoading
        class Succeed(val review: CheckieReview) : ReviewLoading
        class Failed(val error: Throwable) : ReviewLoading
    }

    sealed interface LatestCurrencyLoading : ReviewCreationEvent {
        data object Started : LatestCurrencyLoading
        class Succeed(val currency: CheckieCurrency?) : LatestCurrencyLoading
        class Failed(val error: Throwable) : LatestCurrencyLoading
    }

    sealed interface TagsLoading : ReviewCreationEvent {
        data object Started : TagsLoading
        class Succeed(val tags: List<CheckieTag>) : TagsLoading
        class Failed(val error: Throwable) : TagsLoading
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

        class OnPriceTextInput(val text: String) : ProductInfo

        data object OnPriceCurrencyClick : ProductInfo

        class OnPictureReorder(val fromPosition: Int, val toPosition: Int) : ProductInfo
    }

    sealed interface Tags : ReviewCreationUiEvent {

        class OnSearchQueryInput(val query: String) : Tags

        data object OnSearchQueryClearClick : Tags

        data object OnCreateTagClick : Tags

        class OnTagClick(val tagId: String) : Tags

        class OnTagLongClick(val tagId: String) : Tags
    }

    sealed interface ReviewInfo : ReviewCreationUiEvent {

        class OnRatingSelect(val rating: Int) : ReviewInfo

        class OnCommentInput(val text: String) : ReviewInfo

        class OnAdvantagesInput(val text: String) : ReviewInfo

        class OnDisadvantagesInput(val text: String) : ReviewInfo
    }
}

internal sealed interface ReviewCreationNavigationEvent : ReviewCreationEvent {

    class OnPhotosPick(val uris: List<String>) : ReviewCreationNavigationEvent

    class OnTagCreated(val tagId: String) : ReviewCreationNavigationEvent

    class OnTagDeleted(val tagId: String) : ReviewCreationNavigationEvent

    class OnCurrencySelected(val currency: CheckieCurrency) : ReviewCreationNavigationEvent
}