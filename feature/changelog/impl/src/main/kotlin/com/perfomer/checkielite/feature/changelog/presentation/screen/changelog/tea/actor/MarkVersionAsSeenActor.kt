package com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.ignoreResult
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.ChangelogRepository
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogCommand
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogCommand.MarkVersionAsSeen
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

internal class MarkVersionAsSeenActor(
    private val repository: ChangelogRepository,
) : Actor<ChangelogCommand, ChangelogEvent> {

    override fun act(commands: Flow<ChangelogCommand>): Flow<ChangelogEvent> {
        return commands.filterIsInstance<MarkVersionAsSeen>()
            .mapLatest(::handleCommand)
            .onCatchLog(TAG, "Failed to mark version changelog as seen", rethrow = false)
            .ignoreResult()
    }

    private suspend fun handleCommand(command: MarkVersionAsSeen) {
        repository.setLastSeenVersionCode(command.versionCode)
    }

    private companion object {
        private const val TAG = "MarkChangelogSeenActor"
    }
}
