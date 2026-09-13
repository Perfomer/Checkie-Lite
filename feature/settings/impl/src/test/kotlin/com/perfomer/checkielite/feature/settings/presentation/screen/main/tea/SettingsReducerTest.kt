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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SettingsReducerTest {

    @Test
    fun `initialization observes the app-wide glass setting`() {
        val update = SettingsReducer().reduce(SettingsState(), SettingsEvent.Initialize)

        assertTrue(SettingsCommand.LoadLiquidGlass in update.commands)
    }

    @Test
    fun `saved glass preference updates the switch`() {
        val update = SettingsReducer().reduce(SettingsState(), SettingsEvent.LiquidGlassUpdated(false))

        assertFalse(update.state!!.isLiquidGlassEnabled)
        assertTrue(update.commands.isEmpty())
    }

    @Test
    fun `toggle requests persistence and waits for confirmation`() {
        val update = SettingsReducer().reduce(SettingsState(), SettingsUiEvent.OnLiquidGlassChanged(false))

        assertEquals(listOf(SettingsCommand.SetLiquidGlass(false)), update.commands)
        assertTrue(update.state!!.isLiquidGlassChangeInProgress)
        assertTrue(update.state!!.isLiquidGlassEnabled)
    }

    @Test
    fun `successful save enables further toggles`() {
        val update = SettingsReducer().reduce(
            SettingsState(isLiquidGlassChangeInProgress = true),
            SettingsEvent.LiquidGlassSaved(false),
        )

        assertFalse(update.state!!.isLiquidGlassEnabled)
        assertFalse(update.state!!.isLiquidGlassChangeInProgress)
        assertTrue(update.effects.isEmpty())
    }

    @Test
    fun `toggle can reenable glass`() {
        val update = SettingsReducer().reduce(
            SettingsState(isLiquidGlassEnabled = false),
            SettingsUiEvent.OnLiquidGlassChanged(true),
        )

        assertEquals(listOf(SettingsCommand.SetLiquidGlass(true)), update.commands)
    }

    @Test
    fun `repeated toggles are ignored while saving`() {
        val state = SettingsState(isLiquidGlassChangeInProgress = true)
        val update = SettingsReducer().reduce(
            state,
            SettingsUiEvent.OnLiquidGlassChanged(false),
        )

        assertTrue(update.commands.isEmpty())
        assertEquals(state, update.state)
    }

    @Test
    fun `failure keeps the previous value and allows retry`() {
        val update = SettingsReducer().reduce(
            SettingsState(isLiquidGlassEnabled = false, isLiquidGlassChangeInProgress = true),
            SettingsEvent.LiquidGlassSaveFailed,
        )
        val toast = update.effects.single() as ShowToast

        assertFalse(update.state!!.isLiquidGlassEnabled)
        assertFalse(update.state!!.isLiquidGlassChangeInProgress)
        assertEquals(Text.resource(R.string.settings_toast_liquid_glass_failed), toast.text)
        assertEquals(ToastStyle.ERROR, toast.style)
    }

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
