package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

internal sealed interface BackupCommand {

    data object LoadSettings : BackupCommand

    data object CheckSyncing : BackupCommand

    data object ExportBackup : BackupCommand

    class ImportBackup(val path: String) : BackupCommand
}

internal sealed interface BackupNavigationCommand : BackupCommand {

    data object Exit : BackupNavigationCommand

    data object RestartApp : BackupNavigationCommand
}