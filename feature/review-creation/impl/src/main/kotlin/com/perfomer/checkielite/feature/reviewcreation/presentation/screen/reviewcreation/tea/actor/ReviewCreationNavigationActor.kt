package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor

import com.perfomer.checkielite.common.pure.util.toArrayList
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.DestinationMode
import com.perfomer.checkielite.core.navigation.ExternalDestination
import com.perfomer.checkielite.core.navigation.ExternalResult
import com.perfomer.checkielite.core.navigation.ExternalRouter
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.feature.gallery.navigation.GalleryDestination
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.CurrencySelectorDestination
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.CurrencySelectorResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationDestination
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagSortDestination
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagSortResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenCamera
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenCurrencySelector
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenGallery
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenPhotoPicker
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenTagCreation
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenTagSort
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnCurrencySelected
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnTagCreated
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnTagDeleted
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class ReviewCreationNavigationActor(
    private val router: Router,
    private val externalRouter: ExternalRouter,
) : Actor<ReviewCreationCommand, ReviewCreationEvent> {

    override fun act(commands: Flow<ReviewCreationCommand>): Flow<ReviewCreationEvent> {
        return commands.filterIsInstance<ReviewCreationNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private suspend fun handleCommand(command: ReviewCreationNavigationCommand): ReviewCreationNavigationEvent? = with(router) {
        when (command) {
            is Exit -> exit()
            is ExitWithResult -> exitWithResult(command.result)
            is OpenPhotoPicker -> return openPhotoPicker()
            is OpenCamera -> return openCamera()
            is OpenGallery -> {
                navigate(
                    destination = GalleryDestination(
                        picturesUri = command.picturesUri.toArrayList(),
                        currentPicturePosition = command.currentPicturePosition
                    ),
                    mode = DestinationMode.OVERLAY,
                )
            }
            is OpenTagCreation -> return openTagCreation(command)
            is OpenCurrencySelector -> return openCurrencySelector(command)
            is OpenTagSort -> return openTagSort(command)
        }

        return null
    }

    private suspend fun openPhotoPicker(): ReviewCreationNavigationEvent? {
        val result = externalRouter.navigateForResult<List<String>>(ExternalDestination.GALLERY)
        if (result !is ExternalResult.Success) return null

        return ReviewCreationNavigationEvent.OnPhotosPick(uris = result.result)
    }

    private suspend fun openCamera(): ReviewCreationNavigationEvent? {
        val result = externalRouter.navigateForResult<String>(ExternalDestination.CAMERA)
        if (result !is ExternalResult.Success) return null

        return ReviewCreationNavigationEvent.OnPhotosPick(uris = listOf(result.result))
    }

    private suspend fun openTagCreation(command: OpenTagCreation): ReviewCreationNavigationEvent? {
        val result = router.navigateForResult<TagCreationResult>(
            destination = TagCreationDestination(mode = command.mode),
            mode = DestinationMode.BOTTOM_SHEET,
        )

        return when (result) {
            is TagCreationResult.Created -> OnTagCreated(result.tagId)
            is TagCreationResult.Modified -> null
            is TagCreationResult.Deleted -> OnTagDeleted(result.tagId)
        }
    }

    private suspend fun openTagSort(command: OpenTagSort): ReviewCreationNavigationEvent? {
        val result = router.navigateForResult<TagSortResult>(
            destination = TagSortDestination(command.currentOption),
            mode = DestinationMode.BOTTOM_SHEET,
        )

        return when (result) {
            is TagSortResult.Success -> ReviewCreationNavigationEvent.OnTagSortSelected(result.option)
        }
    }

    private suspend fun openCurrencySelector(command: OpenCurrencySelector): ReviewCreationNavigationEvent? {
        val result = router.navigateForResult<CurrencySelectorResult>(
            destination = CurrencySelectorDestination(command.currency),
            mode = DestinationMode.BOTTOM_SHEET,
        )

        return when (result) {
            is CurrencySelectorResult.Selected -> OnCurrencySelected(result.currency)
        }
    }
}