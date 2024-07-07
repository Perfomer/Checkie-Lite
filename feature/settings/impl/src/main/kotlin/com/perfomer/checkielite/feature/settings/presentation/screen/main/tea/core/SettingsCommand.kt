package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

internal sealed interface SettingsCommand {

    data object LoadSettings : SettingsCommand
}

internal sealed interface SettingsNavigationCommand : SettingsCommand {

    data object Exit : SettingsNavigationCommand
}