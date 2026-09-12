package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.theme.manager.ThemeManager
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.SetLiquidGlass
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.LiquidGlassSaved
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.LiquidGlassSaveFailed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

internal class SetLiquidGlassActor(
    private val themeManager: ThemeManager,
) : Actor<SettingsCommand, SettingsEvent> {

    override fun act(commands: Flow<SettingsCommand>): Flow<SettingsEvent> {
        return commands.filterIsInstance<SetLiquidGlass>()
            .flatMapConcat(::handleCommand)
    }

    private fun handleCommand(command: SetLiquidGlass): Flow<SettingsEvent> {
        return flowBy { themeManager.setLiquidGlassEnabled(command.enabled) }
            .map<Unit, SettingsEvent> { LiquidGlassSaved(command.enabled) }
            .onCatchLog("SetLiquidGlassActor", "Failed to save Liquid Glass preference")
            .catch { emit(LiquidGlassSaveFailed) }
    }
}
