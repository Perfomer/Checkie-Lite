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
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenGallery
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenPhotoPicker
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent
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
        }

        return null
    }

    private suspend fun openPhotoPicker(): ReviewCreationNavigationEvent? {
        val result = externalRouter.navigateForResult(ExternalDestination.GALLERY)
        if (result !is ExternalResult.Success) return null

        return ReviewCreationNavigationEvent.OnPhotoPick(uri = result.path)
    }
}