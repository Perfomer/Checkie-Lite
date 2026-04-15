package com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.lce
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.feature.changelog.domain.repository.ChangelogContentRepository
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogCommand
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogCommand.LoadChangelog
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogEvent
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogEvent.ChangelogStatusUpdated
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class LoadChangelogActor(
    private val repository: ChangelogContentRepository,
) : Actor<ChangelogCommand, ChangelogEvent> {

    override fun act(commands: Flow<ChangelogCommand>): Flow<ChangelogEvent> {
        return commands.filterIsInstance<LoadChangelog>()
            .flatMapLatest(::handleCommand)
            .onCatchLog(TAG, "Failed to load changelog", rethrow = false)
    }

    private fun handleCommand(command: LoadChangelog): Flow<ChangelogEvent> {
        return flowBy { repository.loadChangelog() }
            .lce()
            .map(::ChangelogStatusUpdated)
    }

    private companion object {
        private const val TAG = "LoadChangelogActor"
    }
}
