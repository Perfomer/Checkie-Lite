package com.performer.checkielite.core.theme.manager

import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode
import kotlinx.coroutines.flow.StateFlow

interface ThemeManager {

    val themeMode: StateFlow<ThemeMode>

    suspend fun warmUpThemeMode()

    suspend fun setThemeMode(mode: ThemeMode)
}