package com.perfomer.checkielite.core.theme.manager

import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode
import kotlinx.coroutines.flow.StateFlow

interface ThemeManager {

    val themeMode: StateFlow<ThemeMode>

    val isLiquidGlassEnabled: StateFlow<Boolean>

    suspend fun warmUp()

    suspend fun setThemeMode(mode: ThemeMode)

    suspend fun setLiquidGlassEnabled(enabled: Boolean)
}
