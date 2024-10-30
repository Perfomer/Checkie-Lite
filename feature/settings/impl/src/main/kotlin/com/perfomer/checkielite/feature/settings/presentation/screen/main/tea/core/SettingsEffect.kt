package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

internal sealed interface SettingsEffect {

    sealed interface ShowToast : SettingsEffect {
        class Warning(val reason: WarningReason) : ShowToast
    }

    data object ShowConfirmImportDialog : SettingsEffect
}

internal enum class WarningReason {
    SYNCING_IN_PROGRESS,
}