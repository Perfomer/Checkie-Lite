package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

internal sealed interface BackupEffect {

    sealed interface ShowToast : BackupEffect {

        data object Cancelled : ShowToast

        data object SuccessExport : ShowToast

        class Error(val reason: Reason) : ShowToast {
            enum class Reason {
                COMMON_FAILED_NO_SPACE,
                EXPORT_FAILED_COMMON,
                IMPORT_FAILED_COMMON,
            }
        }
    }
}
