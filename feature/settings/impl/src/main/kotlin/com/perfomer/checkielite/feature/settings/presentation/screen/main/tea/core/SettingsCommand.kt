package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

internal sealed interface SettingsCommand {

    data object LoadSettings : SettingsCommand

    data object CheckSyncing : SettingsCommand

    data object ExportBackup : SettingsCommand

    class ImportBackup(val path: String) : SettingsCommand
}

internal sealed interface SettingsNavigationCommand : SettingsCommand {

    data object Exit : SettingsNavigationCommand

    data object SelectBackupFile : SettingsNavigationCommand
}