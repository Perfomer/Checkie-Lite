package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.LoadTheme
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.ThemeLoaded
import com.performer.checkielite.core.theme.manager.ThemeManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class LoadThemeActor(
    private val themeManager: ThemeManager,
) : Actor<SettingsCommand, SettingsEvent> {

    override fun act(commands: Flow<SettingsCommand>): Flow<SettingsEvent> {
        return commands.filterIsInstance<LoadTheme>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadTheme): Flow<SettingsEvent> {
        return themeManager.themeMode
            .map(::ThemeLoaded)
            .onCatchLog(TAG, "Failed to load theme", rethrow = false)
    }

    private companion object {
        private const val TAG = "LoadThemeActor"
    }
}