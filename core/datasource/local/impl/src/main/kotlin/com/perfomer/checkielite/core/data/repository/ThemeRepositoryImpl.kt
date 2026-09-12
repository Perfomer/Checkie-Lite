package com.perfomer.checkielite.core.data.repository
import com.perfomer.checkielite.core.data.datasource.preferences.PreferencesDataSource
import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode

internal class ThemeRepositoryImpl(
    private val preferencesDataSource: PreferencesDataSource,
) : ThemeRepository {

    override suspend fun getThemeMode(): ThemeMode? {
        return preferencesDataSource.getThemeMode()
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        preferencesDataSource.setThemeMode(themeMode)
    }

    override suspend fun isLiquidGlassEnabled(): Boolean = preferencesDataSource.isLiquidGlassEnabled()

    override suspend fun setLiquidGlassEnabled(enabled: Boolean) {
        preferencesDataSource.setLiquidGlassEnabled(enabled)
    }
}
