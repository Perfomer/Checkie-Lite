package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea

import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.actor.NotifyPictureSelectedActor
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryCommand.NotifyPictureSelected
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryNavigationCommand.Exit
import com.perfomer.checkielite.feature.gallery.presentation.selection.GallerySelectionChannel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class NotifyPictureSelectedActorTest {

    @Test
    fun `publishes positions in order and ignores navigation`() = runBlocking {
        val positions = mutableListOf<Int>()
        val channel = object : GallerySelectionChannel {
            override fun observe(): SharedFlow<Int> = MutableSharedFlow()
            override suspend fun select(position: Int) {
                positions.add(position)
            }
        }
        val events = NotifyPictureSelectedActor(channel)
            .act(flowOf(NotifyPictureSelected(2), Exit, NotifyPictureSelected(0)))
            .toList()

        assertEquals(listOf(2, 0), positions)
        assertTrue(events.isEmpty())
    }
}
