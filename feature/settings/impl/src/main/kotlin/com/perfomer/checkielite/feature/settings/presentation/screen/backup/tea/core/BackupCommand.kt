package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

internal sealed interface BackupCommand {

    data object ObserveBackupProgress : BackupCommand
}

internal sealed interface BackupNavigationCommand : BackupCommand {

    data object Exit : BackupNavigationCommand

    data object RestartApp : BackupNavigationCommand
}