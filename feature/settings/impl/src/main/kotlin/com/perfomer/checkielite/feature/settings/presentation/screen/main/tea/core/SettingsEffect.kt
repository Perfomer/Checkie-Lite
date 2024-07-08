package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

internal sealed interface SettingsEffect {

    sealed interface ShowToast : SettingsEffect {
        class Success(val reason: SuccessReason) : ShowToast
        class Warning(val reason: WarningReason) : ShowToast
        class Error(val reason: ErrorReason) : ShowToast
    }
}

internal enum class SuccessReason {
    BACKUP_COMPLETED,
    RESTORE_COMPLETED,
}

internal enum class ErrorReason {
    NO_PERMISSION_GRANTED,
    NO_FILE_SELECTED,
    UNKNOWN,
}

internal enum class WarningReason {
    BACKUP_IN_PROGRESS,
}