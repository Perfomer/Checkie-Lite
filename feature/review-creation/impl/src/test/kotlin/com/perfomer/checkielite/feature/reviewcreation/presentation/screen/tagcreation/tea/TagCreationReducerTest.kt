package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea

import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastStyle
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagCreationMode
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect.ShowToast
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class TagCreationReducerTest {

    @Test
    fun `saving failure resets progress and shows error`() {
        val update = TagCreationReducer().reduce(
            currentState = TagCreationState(mode = TagCreationMode.Creation(), isSaving = true),
            event = TagCreationEvent.TagSaving.Failed(IllegalStateException()),
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(R.string.tagcreation_error_save), toast.text)
        assertEquals(ToastStyle.ERROR, toast.style)
        assertEquals(false, update.state?.isSaving)
        assertTrue(update.commands.isEmpty())
    }

    @Test
    fun `deletion failure resets progress and shows error`() {
        val update = TagCreationReducer().reduce(
            currentState = TagCreationState(mode = TagCreationMode.Creation(), isDeleting = true),
            event = TagCreationEvent.TagDeletion.Failed(IllegalStateException()),
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(R.string.tagcreation_error_delete), toast.text)
        assertEquals(ToastStyle.ERROR, toast.style)
        assertEquals(false, update.state?.isDeleting)
        assertTrue(update.commands.isEmpty())
    }
}
