package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

internal sealed interface BackupEffect {

    sealed interface ShowToast : BackupEffect {

        data object Cancelled : ShowToast

        data object Success : ShowToast

        class Error(val reason: Reason) : ShowToast {
            enum class Reason {
                EXPORT_FAILED_COMMON,
                EXPORT_FAILED_NO_SPACE,
                IMPORT_FAILED_COMMON,
            }
        }
    }
}
