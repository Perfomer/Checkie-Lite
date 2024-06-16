package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core

import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.price.CheckieCurrency
import com.perfomer.checkielite.core.entity.price.CheckiePrice
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagCreationMode

internal sealed interface ReviewCreationCommand {

    class LoadReview(val reviewId: String) : ReviewCreationCommand

    class SearchBrands(val query: String) : ReviewCreationCommand

    class CreateReview(
        val productName: String,
        val productBrand: String?,
        val price: CheckiePrice?,
        val rating: Int,
        val pictures: List<CheckiePicture>,
        val tagsIds: Set<String>,
        val comment: String?,
        val advantages: String?,
        val disadvantages: String?,
    ) : ReviewCreationCommand

    class UpdateReview(
        val reviewId: String,
        val productName: String,
        val productBrand: String?,
        val price: CheckiePrice?,
        val rating: Int,
        val pictures: List<CheckiePicture>,
        val tagsIds: Set<String>,
        val comment: String?,
        val advantages: String?,
        val disadvantages: String?,
    ) : ReviewCreationCommand

    class LoadTags(val searchQuery: String = "") : ReviewCreationCommand
}

internal sealed interface ReviewCreationNavigationCommand : ReviewCreationCommand {

    data object Exit : ReviewCreationNavigationCommand

    class ExitWithResult(val result: ReviewCreationResult) : ReviewCreationNavigationCommand

    data object OpenPhotoPicker : ReviewCreationNavigationCommand

    class OpenCurrencySelector(val currency: CheckieCurrency) : ReviewCreationNavigationCommand

    class OpenGallery(
        val picturesUri: List<String>,
        val currentPicturePosition: Int,
    ) : ReviewCreationNavigationCommand

    class OpenTagCreation(
        val mode: TagCreationMode,
    ) : ReviewCreationNavigationCommand
}