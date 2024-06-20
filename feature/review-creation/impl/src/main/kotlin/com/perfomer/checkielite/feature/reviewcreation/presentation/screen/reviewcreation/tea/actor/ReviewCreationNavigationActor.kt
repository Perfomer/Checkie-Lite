package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor

import com.perfomer.checkielite.common.pure.util.toArrayList
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.api.DestinationMode
import com.perfomer.checkielite.core.navigation.api.ExternalDestination
import com.perfomer.checkielite.core.navigation.api.ExternalResult
import com.perfomer.checkielite.core.navigation.api.ExternalRouter
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.gallery.navigation.GalleryParams
import com.perfomer.checkielite.feature.gallery.navigation.GalleryScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.CurrencySelectorParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.CurrencySelectorResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.CurrencySelectorScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagSortParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagSortResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagSortScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenCurrencySelector
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenGallery
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenPhotoPicker
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenTagCreation
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenTagSort
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnCurrencySelected
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnTagCreated
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnTagDeleted
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
internal class ReviewCreationNavigationActor(
    private val router: Router,
    private val externalRouter: ExternalRouter,

    private val galleryScreenProvider: GalleryScreenProvider,
    private val tagCreationScreenProvider: TagCreationScreenProvider,
    private val tagSortScreenProvider: TagSortScreenProvider,
    private val currencySelectorScreenProvider: CurrencySelectorScreenProvider,
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
            is OpenGallery -> router.navigate(
                screen = galleryScreenProvider(GalleryParams(command.picturesUri.toArrayList(), command.currentPicturePosition)),
                mode = DestinationMode.OVERLAY,
            )
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

    private suspend fun openTagCreation(command: OpenTagCreation): ReviewCreationNavigationEvent? {
        val params = TagCreationParams(mode = command.mode)
        val result = router.navigateForResult<TagCreationResult>(
            screen = tagCreationScreenProvider(params),
            mode = DestinationMode.BOTTOM_SHEET,
        )

        return when (result) {
            is TagCreationResult.Created -> OnTagCreated(result.tagId)
            is TagCreationResult.Modified -> null
            is TagCreationResult.Deleted -> OnTagDeleted(result.tagId)
        }
    }

    private suspend fun openTagSort(command: OpenTagSort): ReviewCreationNavigationEvent? {
        val params = TagSortParams(command.currentOption)
        val result = router.navigateForResult<TagSortResult>(
            screen = tagSortScreenProvider(params),
            mode = DestinationMode.BOTTOM_SHEET,
        )

        return when (result) {
            is TagSortResult.Success -> ReviewCreationNavigationEvent.OnTagSortSelected(result.option)
        }
    }

    private suspend fun openCurrencySelector(command: OpenCurrencySelector): ReviewCreationNavigationEvent? {
        val params = CurrencySelectorParams(command.currency)
        val result = router.navigateForResult<CurrencySelectorResult>(
            screen = currencySelectorScreenProvider(params),
            mode = DestinationMode.BOTTOM_SHEET,
        )

        return when (result) {
            is CurrencySelectorResult.Selected -> OnCurrencySelected(result.currency)
        }
    }
}