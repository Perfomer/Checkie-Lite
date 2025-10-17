package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode

interface ThemeRepository {

    suspend fun getThemeMode(): ThemeMode?

    suspend fun setThemeMode(themeMode: ThemeMode)
}