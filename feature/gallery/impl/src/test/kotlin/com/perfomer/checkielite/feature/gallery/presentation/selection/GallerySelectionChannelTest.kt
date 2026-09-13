package com.perfomer.checkielite.feature.gallery.presentation.selection

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

internal class GallerySelectionChannelTest {

    @Test
    fun `active observer receives selected position`() = runBlocking {
        val channel = GallerySelectionChannelImpl()
        val selection = async(start = CoroutineStart.UNDISPATCHED) {
            withTimeout(5_000L) { channel.observe().first() }
        }
        channel.select(2)
        assertEquals(2, selection.await())
    }

    @Test
    fun `reopening does not replay previous selection`() = runBlocking {
        val channel = GallerySelectionChannelImpl()
        val previous = async(start = CoroutineStart.UNDISPATCHED) {
            withTimeout(5_000L) { channel.observe().first() }
        }
        channel.select(2)
        assertEquals(2, previous.await())
        val next = async(start = CoroutineStart.UNDISPATCHED) {
            withTimeout(5_000L) { channel.observe().first() }
        }
        assertFalse(next.isCompleted)
        channel.select(1)
        assertEquals(1, next.await())
    }
}
