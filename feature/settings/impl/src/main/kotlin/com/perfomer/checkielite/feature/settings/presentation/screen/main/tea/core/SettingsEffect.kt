package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

internal sealed interface SettingsEffect {

    class ShowToast(val reason: Reason) : SettingsEffect {
        enum class Reason {
            SYNCING_IN_PROGRESS,
            APP_IS_UP_TO_DATE,
            FAILED_TO_CHECK_UPDATES,
        }
    }

    data object ShowConfirmImportDialog : SettingsEffect
}
