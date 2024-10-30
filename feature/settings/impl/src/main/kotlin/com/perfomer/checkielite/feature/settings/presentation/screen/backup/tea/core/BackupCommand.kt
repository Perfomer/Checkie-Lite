package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

internal sealed interface BackupCommand {

    data object ObserveBackupProgress : BackupCommand

    class Await(
        val durationMs: Long,
        val reason: Reason,
    ) : BackupCommand {
        enum class Reason {
            RESTART,
            OPEN_MAIN,
        }
    }

    data object CancelBackup : BackupCommand
}

internal sealed interface BackupNavigationCommand : BackupCommand {

    data object RestartApp : BackupNavigationCommand

    data object OpenMain : BackupNavigationCommand
}