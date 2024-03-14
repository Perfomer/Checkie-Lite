package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core

import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationResult

internal sealed interface ReviewCreationCommand {

    class LoadReview(val reviewId: String) : ReviewCreationCommand

    class SearchBrands(val query: String) : ReviewCreationCommand

    class CreateReview(
        val productName: String,
        val productBrand: String?,
        val rating: Int,
        val pictures: List<CheckiePicture>,
        val reviewText: String?
    ) : ReviewCreationCommand

    class UpdateReview(
        val reviewId: String,
        val productName: String,
        val productBrand: String?,
        val rating: Int,
        val pictures: List<CheckiePicture>,
        val reviewText: String?
    ) : ReviewCreationCommand
}

internal sealed interface ReviewCreationNavigationCommand : ReviewCreationCommand {

    data object Exit : ReviewCreationNavigationCommand

    data class ExitWithResult(val result: ReviewCreationResult) : ReviewCreationNavigationCommand

    data object OpenPhotoPicker : ReviewCreationNavigationCommand

    data class OpenGallery(
        val picturesUri: List<String>,
        val currentPicturePosition: Int,
    ) : ReviewCreationNavigationCommand
}