package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea

import android.util.Log
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.ErrorReason
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.ExportBackup
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.ImportBackup
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.LoadSettings
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect.ShowToast
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.BackupExporting
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.BackupImporting
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.Initialize
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.Exit
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.RequestWriteFileStorageAccess
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.SelectBackupFile
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationEvent.BackupFileSelection
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationEvent.WriteStorageAccessRequest
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsState
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackPress
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupExportClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupImportClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SuccessReason
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.WarningReason

internal class SettingsReducer : DslReducer<SettingsCommand, SettingsEffect, SettingsEvent, SettingsState>() {

    override fun reduce(event: SettingsEvent) = when (event) {
        is Initialize -> reduceInitialize()
        is SettingsUiEvent -> reduceUi(event)
        is SettingsNavigationEvent -> reduceNavigation(event)
        is BackupExporting -> reduceBackupExporting(event)
        is BackupImporting -> reduceBackupImporting(event)
    }

    private fun reduceInitialize() {
        commands(LoadSettings)
    }

    private fun reduceUi(event: SettingsUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
        is OnBackupExportClick -> {
            if (state.isExportingInProgress || state.isImportingInProgress) {
                effects(ShowToast.Warning(WarningReason.BACKUP_IN_PROGRESS))
            } else {
                commands(RequestWriteFileStorageAccess)
            }
        }
        is OnBackupImportClick -> {
            if (state.isExportingInProgress || state.isImportingInProgress) {
                effects(ShowToast.Warning(WarningReason.BACKUP_IN_PROGRESS))
            } else {
                commands(SelectBackupFile)
            }
        }
    }

    private fun reduceNavigation(event: SettingsNavigationEvent) = when (event) {
        is BackupFileSelection -> reduceBackupFileSelection(event)
        is WriteStorageAccessRequest -> reduceWriteStorageAccessRequest(event)
    }

    private fun reduceBackupExporting(event: BackupExporting) = when (event) {
        is BackupExporting.Started -> {
            state { copy(isExportingInProgress = true) }
        }
        is BackupExporting.Succeed -> {
            state { copy(isExportingInProgress = false) }
            effects(ShowToast.Success(SuccessReason.BACKUP_COMPLETED))
        }
        is BackupExporting.Failed -> {
            Log.e(TAG, "Failed to export backup", event.error) // todo move to Actor
            state { copy(isExportingInProgress = false) }
            effects(ShowToast.Error(ErrorReason.UNKNOWN))
        }
    }

    private fun reduceBackupImporting(event: BackupImporting) = when (event) {
        is BackupImporting.Started -> {
            state { copy(isImportingInProgress = true) }
        }
        is BackupImporting.Succeed -> {
            state { copy(isImportingInProgress = false) }
            effects(ShowToast.Success(SuccessReason.RESTORE_COMPLETED))
        }
        is BackupImporting.Failed -> {
            Log.e(TAG, "Failed to import backup", event.error)
            state { copy(isImportingInProgress = false) }
            effects(ShowToast.Error(ErrorReason.UNKNOWN))
        }
    }

    private fun reduceBackupFileSelection(event: BackupFileSelection) = when (event) {
        is BackupFileSelection.Succeed -> commands(ImportBackup(event.path))
        is BackupFileSelection.Canceled -> effects(ShowToast.Error(ErrorReason.NO_FILE_SELECTED))
        is BackupFileSelection.NoPermission -> effects(ShowToast.Error(ErrorReason.NO_PERMISSION_GRANTED))
    }

    private fun reduceWriteStorageAccessRequest(event: WriteStorageAccessRequest) = when (event) {
        is WriteStorageAccessRequest.Granted -> commands(ExportBackup)
        is WriteStorageAccessRequest.Denied -> effects(ShowToast.Error(ErrorReason.NO_PERMISSION_GRANTED))
    }

    private companion object {
        private const val TAG = "SettingsReducer"
    }
}