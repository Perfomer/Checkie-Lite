package com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeCommand.SetTheme
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeEvent.ThemeSetSucceed
import com.performer.checkielite.core.theme.manager.ThemeManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class SetThemeActor(
    private val themeManager: ThemeManager,
) : Actor<ThemeCommand, ThemeEvent> {

    override fun act(commands: Flow<ThemeCommand>): Flow<ThemeEvent> {
        return commands.filterIsInstance<SetTheme>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: SetTheme): Flow<ThemeEvent> {
        return flowBy { themeManager.setThemeMode(command.themeMode) }
            .map { ThemeSetSucceed(command.themeMode) }
            .onCatchLog(TAG, "Failed to set selected theme", rethrow = false)
    }

    private companion object {
        private const val TAG = "SetThemeActor"
    }
}