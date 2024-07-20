package com.perfomer.checkielite.feature.settings.presentation.navigation

import com.perfomer.checkielite.core.entity.backup.BackupMode
import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params

data class BackupParams(
    val mode: BackupMode,
) : Params

fun interface BackupScreenProvider {

    operator fun invoke(params: BackupParams): CheckieScreen
}