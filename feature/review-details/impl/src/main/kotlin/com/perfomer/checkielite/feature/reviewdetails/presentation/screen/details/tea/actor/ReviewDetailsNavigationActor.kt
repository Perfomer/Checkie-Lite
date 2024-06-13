package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.actor

import com.perfomer.checkielite.common.pure.util.toArrayList
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.api.DestinationMode
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.gallery.navigation.GalleryParams
import com.perfomer.checkielite.feature.gallery.navigation.GalleryScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationMode
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationParams
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationScreenProvider
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsParams
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsScreenProvider
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.OpenGallery
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.OpenReviewDetails
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.OpenReviewEdit
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.OpenSearch
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchParams
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchScreenProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
internal class ReviewDetailsNavigationActor(
    private val router: Router,
    private val reviewCreationScreenProvider: ReviewCreationScreenProvider,
    private val reviewDetailsScreenProvider: ReviewDetailsScreenProvider,
    private val galleryScreenProvider: GalleryScreenProvider,
    private val searchScreenProvider: SearchScreenProvider,
) : Actor<ReviewDetailsCommand, ReviewDetailsEvent> {

    override fun act(commands: Flow<ReviewDetailsCommand>): Flow<ReviewDetailsEvent> {
        return commands.filterIsInstance<ReviewDetailsNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private fun handleCommand(command: ReviewDetailsNavigationCommand): ReviewDetailsEvent? = with(router) {
        when (command) {
            is Exit -> {
                exit()
            }

            is OpenReviewEdit -> {
                val params = ReviewCreationParams(mode = ReviewCreationMode.Modification(reviewId = command.reviewId, initialPage = command.initialPage))
                navigate(reviewCreationScreenProvider(params))
            }

            is OpenReviewDetails -> {
                val params = ReviewDetailsParams(reviewId = command.reviewId)
                navigate(reviewDetailsScreenProvider(params))
            }

            is OpenGallery -> {
                val params = GalleryParams(picturesUri = command.picturesUri.toArrayList(), currentPicturePosition = command.currentPicturePosition)
                navigate(screen = galleryScreenProvider(params), mode = DestinationMode.OVERLAY)
            }

            is OpenSearch -> {
                val params = SearchParams(tags = arrayListOf(command.tag))
                navigate(searchScreenProvider(params))
            }
        }

        return null
    }
}