package com.perfomer.checkielite.navigation

import androidx.lifecycle.LifecycleCoroutineScope
import com.perfomer.checkielite.core.data.repository.BackupRepository
import com.perfomer.checkielite.core.entity.backup.BackupMode
import com.perfomer.checkielite.core.entity.backup.BackupProgress
import com.perfomer.checkielite.core.entity.backup.BackupState
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.settings.presentation.navigation.BackupParams
import com.perfomer.checkielite.feature.settings.presentation.navigation.BackupScreenProvider
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class BackupNavigationManager(
    private val router: Router,
    private val backupScreenProvider: BackupScreenProvider,
    private val backupRepository: BackupRepository,
) {

    fun register(scope: LifecycleCoroutineScope) {
        scope.launch {
            backupRepository.observeBackupState()
                .map { state -> state.mode to (state.progress is BackupProgress.InProgress) }
                .distinctUntilChanged()
                .collect { (mode, isBackupInProgress) ->
                    if (isBackupInProgress && mode != null) {
                        onBackupStarted(mode)
                    }
                }
        }
    }

    fun getCurrentBackupState(): BackupState {
        return backupRepository.backupState
    }

    private fun onBackupStarted(mode: BackupMode) {
        router.replaceStack(backupScreenProvider(BackupParams(mode)))
    }
}