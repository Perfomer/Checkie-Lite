package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

import com.perfomer.checkielite.core.entity.backup.BackupMode

internal sealed interface BackupEffect {

    sealed interface ShowToast : BackupEffect {
        data object Success : ShowToast
        class Error(val mode: BackupMode) : ShowToast
    }
}
