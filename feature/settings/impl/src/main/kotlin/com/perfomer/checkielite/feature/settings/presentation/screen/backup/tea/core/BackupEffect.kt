package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

internal sealed interface BackupEffect {

    class ShowToast(val reason: Reason) : BackupEffect {
        enum class Reason {
            EXPORT_SUCCESS,
            // `IMPORT_SUCCESS` shows only after app restart: see `RestartAction.ShowSuccessBackupImportToast`.
            IMPORT_CANCELLED,
            EXPORT_CANCELLED,
            BACKUP_FAILED_NO_SPACE,
            EXPORT_FAILED_COMMON,
            IMPORT_FAILED_COMMON,
            IMPORT_FAILED_UPDATE_REQUIRED,
        }
    }
}
