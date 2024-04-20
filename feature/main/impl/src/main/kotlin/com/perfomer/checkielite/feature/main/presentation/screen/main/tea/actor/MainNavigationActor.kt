package com.perfomer.checkielite.feature.main.presentation.screen.main.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationCommand
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationCommand.OpenReviewCreation
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationCommand.OpenReviewDetails
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationCommand.OpenSearch
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationEvent.ReviewCreated
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationMode
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationParams
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationResult
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationScreenProvider
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsParams
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsScreenProvider
import com.perfomer.checkielite.feature.search.navigation.SearchParams
import com.perfomer.checkielite.feature.search.navigation.SearchScreenProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
internal class MainNavigationActor(
    private val router: Router,
    private val reviewCreationScreenProvider: ReviewCreationScreenProvider,
    private val reviewDetailsScreenProvider: ReviewDetailsScreenProvider,
    private val searchScreenProvider: SearchScreenProvider,
) : Actor<MainCommand, MainEvent> {

    override fun act(commands: Flow<MainCommand>): Flow<MainEvent> {
        return commands.filterIsInstance<MainNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private suspend fun handleCommand(command: MainNavigationCommand): MainEvent? = with(router) {
        when (command) {
            is OpenReviewCreation -> {
                val params = ReviewCreationParams(ReviewCreationMode.Creation)
                navigateForResult<ReviewCreationResult>(reviewCreationScreenProvider(params))
                return ReviewCreated
            }

            is OpenReviewDetails -> {
                val params = ReviewDetailsParams(command.reviewId)
                navigate(reviewDetailsScreenProvider(params))
            }

            is OpenSearch -> {
                val params = SearchParams(arrayListOf())
                navigate(searchScreenProvider(params))
            }
        }

        return null
    }
}