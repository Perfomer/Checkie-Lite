package com.perfomer.checkielite.feature.main.presentation.screen.main.tea

import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastStyle
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.feature.main.R
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEffect.ShowToast
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class MainReducerTest {

    @Test
    fun `created review produces success toast`() {
        val update = MainReducer().reduce(
            currentState = MainState(),
            event = MainNavigationEvent.ReviewCreated,
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(R.string.main_toast_reviewcreated), toast.text)
        assertEquals(ToastStyle.SUCCESS, toast.style)
        assertTrue(update.commands.isEmpty())
    }
}
