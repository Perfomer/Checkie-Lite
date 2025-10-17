package com.performer.checkielite.core.theme.manager

import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode

interface ThemeManager {

    suspend fun loadThemeMode()

    suspend fun setThemeMode(mode: ThemeMode)
}