package com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state

import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsState
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class SettingsUiStateMapperTest {

    @ParameterizedTest
    @CsvSource("true, false", "false, false", "true, true", "false, true")
    fun `glass preference and saving state reach the UI`(enabled: Boolean, saving: Boolean) {
        val state = SettingsState(isLiquidGlassEnabled = enabled, isLiquidGlassChangeInProgress = saving)

        val uiState = SettingsUiStateMapper(mockk()).map(state)

        assertEquals(enabled, uiState.isLiquidGlassEnabled)
        assertEquals(saving, uiState.isLiquidGlassChangeInProgress)
    }
}
