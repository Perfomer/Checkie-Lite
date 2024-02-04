package com.perfomer.checkielite.feature.main.presentation.tea.actor

import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainCommand
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainEvent
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainNavigationCommand
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainNavigationCommand.OpenReviewCreation
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainNavigationCommand.OpenReviewDetails
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationScreenProvider
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsScreenProvider
import com.perfomer.checkielite.tea.tea.component.Actor
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
) : Actor<MainCommand, MainEvent> {

    override fun act(commands: Flow<MainCommand>): Flow<MainEvent> {
        return commands.filterIsInstance<MainNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private fun handleCommand(command: MainNavigationCommand): MainEvent? = with(router) {
        when (command) {
            is OpenReviewCreation -> navigate(reviewCreationScreenProvider())
            is OpenReviewDetails -> navigate(reviewDetailsScreenProvider())
        }

        return null
    }
}