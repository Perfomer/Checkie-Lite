package com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state

import android.content.Context
import android.content.pm.PackageManager
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsState


internal class SettingsUiStateMapper(
    private val context: Context,
) : UiStateMapper<SettingsState, SettingsUiState> {

    override fun map(state: SettingsState): SettingsUiState {
        return SettingsUiState(
            appVersion = getVersionName(),
        )
    }

    private fun getVersionName(): String {
        return try {
            context.packageManager
                .getPackageInfo(context.packageName, 0)
                .versionName!!
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "Unknown"
        }
    }
}