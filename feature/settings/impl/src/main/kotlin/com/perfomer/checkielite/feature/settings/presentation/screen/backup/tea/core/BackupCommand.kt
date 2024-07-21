package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

internal sealed interface BackupCommand {

    data object ObserveBackupProgress : BackupCommand

    class Await(val durationMs: Long) : BackupCommand
}

internal sealed interface BackupNavigationCommand : BackupCommand {

    data object OpenMain : BackupNavigationCommand
}