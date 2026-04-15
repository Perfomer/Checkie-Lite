package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor

import android.content.Context
import com.perfomer.checkielite.common.android.util.currentLocale
import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.LoadCurrentLocale
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.CurrentLocaleUpdated
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow

internal class LoadCurrentLocaleActor(
    private val context: Context,
) : Actor<SettingsCommand, SettingsEvent> {

    override fun act(commands: Flow<SettingsCommand>): Flow<SettingsEvent> {
        return commands.filterIsInstance<LoadCurrentLocale>()
            .handleCommand()
    }

    private fun Flow<LoadCurrentLocale>.handleCommand(): Flow<SettingsEvent> = flow {
        collect {
            emit(CurrentLocaleUpdated(context.currentLocale))
        }
    }.onCatchLog(TAG, "Failed to load current locale", rethrow = false)

    private companion object {
        private const val TAG = "LoadCurrentLocaleActor"
    }
}
