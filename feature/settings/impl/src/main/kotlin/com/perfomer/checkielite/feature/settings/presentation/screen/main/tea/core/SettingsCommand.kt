package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

import com.perfomer.checkielite.feature.settings.presentation.entity.BackupMode

internal sealed interface SettingsCommand {

    data object LoadSettings : SettingsCommand

    data object CheckSyncing : SettingsCommand

    data object ExportBackup : SettingsCommand

    class ImportBackup(val path: String) : SettingsCommand
}

internal sealed interface SettingsNavigationCommand : SettingsCommand {

    data object Exit : SettingsNavigationCommand

    data object RestartApp : SettingsNavigationCommand

    data object SelectBackupFile : SettingsNavigationCommand

    class OpenBackup(val mode: BackupMode) : SettingsNavigationCommand
}