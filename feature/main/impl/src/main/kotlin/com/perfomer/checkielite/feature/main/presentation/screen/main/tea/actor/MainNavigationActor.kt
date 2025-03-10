package com.perfomer.checkielite.feature.main.presentation.screen.main.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationCommand
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationCommand.OpenReviewCreation
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationCommand.OpenReviewDetails
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationCommand.OpenSearch
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationCommand.OpenSettings
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationEvent.ReviewCreated
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationMode
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationDestination
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationResult
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsDestination
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchDestination
import com.perfomer.checkielite.feature.settings.presentation.navigation.SettingsDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class MainNavigationActor(
    private val router: Router,
) : Actor<MainCommand, MainEvent> {

    override fun act(commands: Flow<MainCommand>): Flow<MainEvent> {
        return commands.filterIsInstance<MainNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private suspend fun handleCommand(command: MainNavigationCommand): MainEvent? = with(router) {
        when (command) {
            is OpenReviewCreation -> {
                navigateForResult<ReviewCreationResult>(
                    ReviewCreationDestination(ReviewCreationMode.Creation),
                )

                return ReviewCreated
            }

            is OpenReviewDetails -> navigate(ReviewDetailsDestination(command.reviewId))
            is OpenSearch -> navigate(SearchDestination(tagId = command.tagId))
            is OpenSettings -> navigate(SettingsDestination)
        }

        return null
    }
}