package com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.cui.widget.toast.LocalToastController
import com.perfomer.checkielite.common.ui.cui.widget.toast.showToast
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.BackupStore
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEffect.ShowToast
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent.OnBackPress
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent.OnCancelClick

internal class BackupContentScreen(private val store: BackupStore) : Screen {

    @Composable
    override fun Screen() = TeaComposable(store) { state ->
        BackHandler { accept(OnBackPress) }

        val toastController = LocalToastController.current

        EffectHandler { effect ->
            when (effect) {
                is ShowToast -> toastController.showToast(
                    message = effect.text,
                    style = effect.style,
                )
            }
        }

        BackupScreen(
            state = state,
            onCancelClick = acceptable(OnCancelClick),
        )
    }
}