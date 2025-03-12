package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.ReviewRepository
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.CheckHasReviews
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.CheckingHasReviewsStatusUpdated
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class CheckHasReviewsActor(
    private val reviewRepository: ReviewRepository,
) : Actor<SettingsCommand, SettingsEvent> {

    override fun act(commands: Flow<SettingsCommand>): Flow<SettingsEvent> {
        return commands.filterIsInstance<CheckHasReviews>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: CheckHasReviews): Flow<SettingsEvent> {
        return reviewRepository.getReviews()
            .map { it.isNotEmpty() }
            .map(::CheckingHasReviewsStatusUpdated)
            .onCatchLog(TAG, "Failed to check if has reviews", rethrow = false)
    }

    private companion object {
        private const val TAG = "CheckHasReviewsActor"
    }
}