package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

internal data class SettingsState(
    val isExportingInProgress: Boolean = false,
    val isImportingInProgress: Boolean = false,
    val isSyncingInProgress: Boolean = false,
) {
    val isBackupInProgress: Boolean = isExportingInProgress || isImportingInProgress
}