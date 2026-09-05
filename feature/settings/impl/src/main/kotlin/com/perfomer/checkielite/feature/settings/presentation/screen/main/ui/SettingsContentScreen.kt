package com.perfomer.checkielite.feature.settings.presentation.screen.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.cui.widget.toast.LocalToastController
import com.perfomer.checkielite.common.ui.cui.widget.toast.showToast
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.SettingsStore
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect.ShowConfirmImportDialog
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect.ShowToast
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackPress
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupExportClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupImportClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupImportConfirmClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnChangelogClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnCheckUpdatesClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnLanguageSettingsClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnLibrariesClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnStart
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnThemeSettingsClick

internal class SettingsContentScreen(private val store: SettingsStore) : Screen {

    @Composable
    override fun Screen() = TeaComposable(store) { state ->
        LaunchedEffect(Unit) {
            accept(OnStart)
        }

        val toastController = LocalToastController.current

        var shouldShowBackupImportConfirmDialog by remember { mutableStateOf(false) }

        EffectHandler { effect ->
            when (effect) {
                is ShowConfirmImportDialog -> shouldShowBackupImportConfirmDialog = true
                is ShowToast -> toastController.showToast(
                    message = effect.text,
                    style = effect.style,
                )
            }
        }

        SettingsScreen(
            state = state,

            shouldShowBackupImportConfirmDialog = shouldShowBackupImportConfirmDialog,
            onBackupImportConfirmDialogConfirm = acceptable(OnBackupImportConfirmClick),
            onBackupImportConfirmDialogDismiss = { shouldShowBackupImportConfirmDialog = false },

            onNavigationIconClick = acceptable(OnBackPress),
            onBackupExportClick = acceptable(OnBackupExportClick),
            onBackupImportClick = acceptable(OnBackupImportClick),
            onCheckUpdatesClick = acceptable(OnCheckUpdatesClick),
            onChangelogClick = acceptable(OnChangelogClick),
            onLanguageSettingsClick = acceptable(OnLanguageSettingsClick),
            onThemeSettingsClick = acceptable(OnThemeSettingsClick),
            onLibrariesClick = acceptable(OnLibrariesClick),
        )
    }
}