package com.performer.checkielite.core.theme.manager

import com.perfomer.checkielite.core.data.repository.ThemeRepository
import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class ThemeManagerImpl(
    private val themeRepository: ThemeRepository,
) : ThemeManager {

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    override val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    override suspend fun loadThemeMode() {
        val mode = themeRepository.getThemeMode() ?: ThemeMode.SYSTEM
        _themeMode.value = mode
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        themeRepository.setThemeMode(mode)
        _themeMode.value = mode
    }
}