package com.perfomer.checkielite.navigation

import com.perfomer.checkielite.core.domain.entity.backup.BackupProgress
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.feature.main.navigation.MainDestination
import com.perfomer.checkielite.feature.settings.presentation.navigation.BackupDestination

internal class StartScreenProvider(
    private val backupNavigationManager: BackupNavigationManager,
) {

    operator fun invoke(): Destination {
        val backupState = backupNavigationManager.getCurrentBackupState()

        val backupMode = backupState.mode
        val isBackupInProgress = backupState.progress is BackupProgress.InProgress

        return if (isBackupInProgress && backupMode != null) {
            BackupDestination(backupMode)
        } else {
            MainDestination
        }
    }
}