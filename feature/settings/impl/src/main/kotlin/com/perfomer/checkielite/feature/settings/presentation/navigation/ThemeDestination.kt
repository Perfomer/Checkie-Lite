package com.perfomer.checkielite.feature.settings.presentation.navigation

import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode
import com.perfomer.checkielite.core.navigation.Destination
import kotlinx.serialization.Serializable

@Serializable
class ThemeDestination(
    val currentTheme: ThemeMode,
) : Destination()