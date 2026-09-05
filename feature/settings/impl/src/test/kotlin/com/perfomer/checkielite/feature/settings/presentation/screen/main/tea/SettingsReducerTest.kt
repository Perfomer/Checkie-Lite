package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastStyle
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.feature.settings.R
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect.ShowToast
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsState
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SettingsReducerTest {

    @Test
    fun `no updates produces success toast`() {
        val update = SettingsReducer().reduce(
            currentState = SettingsState(isSyncingInProgress = true),
            event = SettingsEvent.UpdatesCheck(Lce.Content(false)),
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(R.string.settings_toast_update_check_succeed), toast.text)
        assertEquals(ToastStyle.SUCCESS, toast.style)
        assertTrue(update.commands.isEmpty())
    }

    @Test
    fun `update failure produces error toast`() {
        val update = SettingsReducer().reduce(
            currentState = SettingsState(isSyncingInProgress = true),
            event = SettingsEvent.UpdatesCheck(Lce.Error()),
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(R.string.settings_toast_update_check_failed), toast.text)
        assertEquals(ToastStyle.ERROR, toast.style)
        assertTrue(update.commands.isEmpty())
    }

    @Test
    fun `export during sync produces warning`() {
        val update = SettingsReducer().reduce(
            currentState = SettingsState(isSyncingInProgress = true),
            event = SettingsUiEvent.OnBackupExportClick,
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(CommonString.common_toast_syncing), toast.text)
        assertEquals(ToastStyle.WARNING, toast.style)
        assertTrue(update.commands.isEmpty())
    }

    @Test
    fun `import during sync produces warning`() {
        val update = SettingsReducer().reduce(
            currentState = SettingsState(isSyncingInProgress = true),
            event = SettingsUiEvent.OnBackupImportClick,
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(CommonString.common_toast_syncing), toast.text)
        assertEquals(ToastStyle.WARNING, toast.style)
        assertTrue(update.commands.isEmpty())
    }

    @Test
    fun `available update launches updater without toast`() {
        val update = SettingsReducer().reduce(SettingsState(), SettingsEvent.UpdatesCheck(Lce.Content(true)))

        assertTrue(update.effects.isEmpty())
        assertEquals(listOf(SettingsCommand.LaunchAppUpdate), update.commands)
    }
}
