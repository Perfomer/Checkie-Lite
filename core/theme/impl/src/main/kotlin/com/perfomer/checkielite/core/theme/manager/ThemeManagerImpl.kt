package com.perfomer.checkielite.core.theme.manager

import com.perfomer.checkielite.core.data.repository.ThemeRepository
import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

internal class ThemeManagerImpl(
    private val themeRepository: ThemeRepository,
) : ThemeManager {

    private val settingsMutex = Mutex()

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    override val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _isLiquidGlassEnabled = MutableStateFlow(true)
    override val isLiquidGlassEnabled: StateFlow<Boolean> = _isLiquidGlassEnabled.asStateFlow()

    override suspend fun warmUp() = settingsMutex.withLock {
        val mode = themeRepository.getThemeMode() ?: ThemeMode.SYSTEM
        val liquidGlassEnabled = themeRepository.isLiquidGlassEnabled()
        _themeMode.value = mode
        _isLiquidGlassEnabled.value = liquidGlassEnabled
    }

    override suspend fun setThemeMode(mode: ThemeMode) = settingsMutex.withLock {
        themeRepository.setThemeMode(mode)
        _themeMode.value = mode
    }

    override suspend fun setLiquidGlassEnabled(enabled: Boolean) = settingsMutex.withLock {
        // Closing Settings must not leave the stored preference ahead of the app-wide state.
        withContext(NonCancellable) {
            themeRepository.setLiquidGlassEnabled(enabled)
            _isLiquidGlassEnabled.value = enabled
        }
    }
}
