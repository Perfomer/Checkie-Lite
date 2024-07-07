package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.LoadSettings
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.Initialize
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.Exit
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsState
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackPress

internal class SettingsReducer : DslReducer<SettingsCommand, SettingsEffect, SettingsEvent, SettingsState>() {

    override fun reduce(event: SettingsEvent) = when (event) {
        is Initialize -> reduceInitialize()
        is SettingsUiEvent -> reduceUi(event)
        is SettingsNavigationEvent -> reduceNavigation(event)
    }

    private fun reduceInitialize() {
        commands(LoadSettings)
    }

    private fun reduceUi(event: SettingsUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
    }

    private fun reduceNavigation(event: SettingsNavigationEvent) = when (event) {
        else -> Unit
    }
}