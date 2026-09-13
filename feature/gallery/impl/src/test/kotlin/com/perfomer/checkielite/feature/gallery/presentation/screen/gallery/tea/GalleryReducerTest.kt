package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea

import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryCommand
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryState
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryUiEvent
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class GalleryReducerTest {

    @Test
    fun `swiping to third photo publishes selection before exit`() {
        val update = GalleryReducer().reduce(
            currentState = GalleryState(picturesUri = persistentListOf("first", "second", "third")),
            event = GalleryUiEvent.OnPictureSelect(2),
        )

        assertEquals(2, requireNotNull(update.state).currentPicturePosition)
        assertEquals(2, (update.commands.single() as GalleryCommand.NotifyPictureSelected).position)
        assertTrue(update.effects.isEmpty())
    }

    @Test
    fun `returning to first photo notifies opener`() {
        val update = GalleryReducer().reduce(
            currentState = GalleryState(
                picturesUri = persistentListOf("first", "second", "third"),
                currentPicturePosition = 2,
            ),
            event = GalleryUiEvent.OnPictureSelect(0),
        )
        assertEquals(0, requireNotNull(update.state).currentPicturePosition)
        assertEquals(0, (update.commands.single() as GalleryCommand.NotifyPictureSelected).position)
    }
}
