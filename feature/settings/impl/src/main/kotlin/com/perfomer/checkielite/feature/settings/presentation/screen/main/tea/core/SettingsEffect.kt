package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

internal sealed interface SettingsEffect {

    sealed interface ShowToast : SettingsEffect {
        class Warning(val reason: WarningReason) : ShowToast
        data object Error : ShowToast
    }
}

internal enum class WarningReason {
    BACKUP_IN_PROGRESS,
    SYNCING_IN_PROGRESS,
}