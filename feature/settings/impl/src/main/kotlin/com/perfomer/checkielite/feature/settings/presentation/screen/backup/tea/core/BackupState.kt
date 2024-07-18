package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

internal data class BackupState(
    val isExportingInProgress: Boolean = false,
    val isImportingInProgress: Boolean = false,
    val isSyncingInProgress: Boolean = false,
) {
    val isBackupInProgress: Boolean = isExportingInProgress || isImportingInProgress
}