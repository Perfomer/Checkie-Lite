package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.CheckSyncing
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.LoadSettings
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.BackupExporting
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.BackupImporting
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.Initialize
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupNavigationCommand.Exit
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupNavigationCommand.RestartApp
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupState
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent.OnBackPress

internal class BackupReducer : DslReducer<BackupCommand, BackupEffect, BackupEvent, BackupState>() {

    override fun reduce(event: BackupEvent) = when (event) {
        is Initialize -> reduceInitialize()
        is BackupUiEvent -> reduceUi(event)
        is BackupExporting -> reduceBackupExporting(event)
        is BackupImporting -> reduceBackupImporting(event)
    }

    private fun reduceInitialize() {
        commands(LoadSettings, CheckSyncing)
    }

    private fun reduceUi(event: BackupUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
    }

    private fun reduceBackupExporting(event: BackupExporting) = when (event) {
        is BackupExporting.Started -> {
            state { copy(isExportingInProgress = true) }
        }
        is BackupExporting.Succeed -> {
            state { copy(isExportingInProgress = false) }
        }
        is BackupExporting.Failed -> {
            state { copy(isExportingInProgress = false) }
        }
    }

    private fun reduceBackupImporting(event: BackupImporting) = when (event) {
        is BackupImporting.Started -> {
            state { copy(isImportingInProgress = true) }
        }
        is BackupImporting.Succeed -> {
            state { copy(isImportingInProgress = false) }
            commands(RestartApp)
        }
        is BackupImporting.Failed -> {
            state { copy(isImportingInProgress = false) }
        }
    }
}