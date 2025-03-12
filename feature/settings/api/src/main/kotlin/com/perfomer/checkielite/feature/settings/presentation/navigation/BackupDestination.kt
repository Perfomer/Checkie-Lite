package com.perfomer.checkielite.feature.settings.presentation.navigation

import com.perfomer.checkielite.core.domain.entity.backup.BackupMode
import com.perfomer.checkielite.core.navigation.Destination
import kotlinx.serialization.Serializable

@Serializable
class BackupDestination(
    val mode: BackupMode,
) : Destination()