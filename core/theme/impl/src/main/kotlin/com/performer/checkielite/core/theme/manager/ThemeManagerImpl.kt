package com.performer.checkielite.core.theme.manager

import com.perfomer.checkielite.core.data.repository.ThemeRepository
import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode
import com.performer.checkielite.core.theme.holder.ThemeHolder

internal class ThemeManagerImpl(
    private val themeRepository: ThemeRepository,
    private val stateHolder: ThemeHolder,
) : ThemeManager {

    override suspend fun loadThemeMode() {
        val mode = themeRepository.getThemeMode() ?: ThemeMode.SYSTEM

        stateHolder.setThemeMode(mode)
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        themeRepository.setThemeMode(mode)
        stateHolder.setThemeMode(mode)
    }
}