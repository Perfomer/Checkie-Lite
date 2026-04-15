package com.perfomer.checkielite.feature.main.presentation.screen.main.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.appInfo.AppInfo
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.ChangelogRepository
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand.CheckAppUpdatedRecently
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent.AppRecentUpdateChecked
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class CheckAppUpdatedRecentlyActor(
    private val repository: ChangelogRepository,
) : Actor<MainCommand, MainEvent> {

    override fun act(commands: Flow<MainCommand>): Flow<MainEvent> {
        return commands.filterIsInstance<CheckAppUpdatedRecently>()
            .flatMapLatest(::handleCommand)
            .onCatchLog(TAG, "Failed to load changelog visibility", rethrow = false)
    }

    private fun handleCommand(command: CheckAppUpdatedRecently): Flow<MainEvent> {
        return flowBy {
            val lastSeenVersionCode = repository.getLastSeenVersionCode()
            lastSeenVersionCode == null || lastSeenVersionCode < AppInfo.versionCode
        }.map(::AppRecentUpdateChecked)
    }

    private companion object {
        private const val TAG = "LoadChangelogVisibilityActor"
    }
}
