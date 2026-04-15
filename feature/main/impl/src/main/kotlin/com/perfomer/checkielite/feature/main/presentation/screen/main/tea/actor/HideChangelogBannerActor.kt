package com.perfomer.checkielite.feature.main.presentation.screen.main.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.appInfo.AppInfo
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.ignoreResult
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.ChangelogRepository
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand.HideChangelogBanner
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest

internal class HideChangelogBannerActor(
    private val repository: ChangelogRepository,
) : Actor<MainCommand, MainEvent> {

    override fun act(commands: Flow<MainCommand>): Flow<MainEvent> {
        return commands.filterIsInstance<HideChangelogBanner>()
            .flatMapLatest(::handleCommand)
            .onCatchLog(TAG, "Failed to hide changelog banner", rethrow = false)
    }

    private fun handleCommand(command: HideChangelogBanner): Flow<MainEvent> {
        return flowBy {
            repository.setLastSeenVersionCode(AppInfo.versionCode)
        }.ignoreResult()
    }

    private companion object {
        private const val TAG = "HideChangelogBannerActor"
    }
}
