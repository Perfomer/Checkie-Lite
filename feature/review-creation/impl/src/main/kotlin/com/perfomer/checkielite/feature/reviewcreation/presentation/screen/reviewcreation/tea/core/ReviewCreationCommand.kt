package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core

import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagCreationMode

internal sealed interface ReviewCreationCommand {

    data object WarmUpEmojis : ReviewCreationCommand

    class LoadReview(val reviewId: String) : ReviewCreationCommand

    class SearchBrands(val query: String) : ReviewCreationCommand

    class CreateReview(
        val productName: String,
        val productBrand: String?,
        val rating: Int,
        val pictures: List<CheckiePicture>,
        val tags: List<CheckieTag>,
        val comment: String?,
        val advantages: String?,
        val disadvantages: String?,
    ) : ReviewCreationCommand

    class UpdateReview(
        val reviewId: String,
        val productName: String,
        val productBrand: String?,
        val rating: Int,
        val pictures: List<CheckiePicture>,
        val tags: List<CheckieTag>,
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

    class OpenGallery(
        val picturesUri: List<String>,
        val currentPicturePosition: Int,
    ) : ReviewCreationNavigationCommand

    class OpenTagCreation(
        val mode: TagCreationMode,
    ) : ReviewCreationNavigationCommand
}