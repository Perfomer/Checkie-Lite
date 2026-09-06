package com.perfomer.checkielite.navigation.decompose

import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.backhandler.BackEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SharedTransitionBackHandlerTest {

    @Test
    fun `cancellation seeks back to zero before resetting the transition`() = runTest {
        val dispatcher = BackDispatcher()
        val events = mutableListOf<String>()
        val callback = recordingCallback(events)
        val handler = SharedTransitionBackHandler(
            delegate = dispatcher,
            scope = backgroundScope,
            animateCancellation = { event, onProgress ->
                events += "rollback:${event.progress}"
                delay(100L)
                onProgress(event.copy(progress = 0F))
            },
            awaitFrame = { delay(16L) },
        )
        handler.register(callback)

        dispatcher.startPredictiveBack(BackEvent())
        dispatcher.progressPredictiveBack(BackEvent(progress = 0.6F))
        dispatcher.cancelPredictiveBack()
        runCurrent()

        assertEquals(listOf("start", "progress:0.6", "rollback:0.6"), events)
        advanceTimeBy(100L)
        runCurrent()
        assertEquals("progress:0.0", events.last())
        assertFalse("cancel" in events)
        advanceTimeBy(16L)
        runCurrent()
        assertEquals("cancel", events.last())
        assertFalse("back" in events)
        handler.unregister(callback)
    }

    @Test
    fun `rapid next gesture waits for rollback and completes exactly once`() = runTest {
        val dispatcher = BackDispatcher()
        val events = mutableListOf<String>()
        val callback = recordingCallback(events)
        val handler = SharedTransitionBackHandler(
            delegate = dispatcher,
            scope = backgroundScope,
            animateCancellation = { event, onProgress ->
                delay(100L)
                onProgress(event.copy(progress = 0F))
            },
            awaitFrame = { delay(16L) },
        )
        handler.register(callback)

        dispatcher.startPredictiveBack(BackEvent())
        dispatcher.progressPredictiveBack(BackEvent(progress = 0.6F))
        dispatcher.cancelPredictiveBack()
        dispatcher.startPredictiveBack(BackEvent())
        dispatcher.progressPredictiveBack(BackEvent(progress = 0.3F))
        dispatcher.back()
        runCurrent()

        assertEquals(listOf("start", "progress:0.6"), events)
        advanceTimeBy(132L)
        runCurrent()
        assertEquals(
            listOf("start", "progress:0.6", "progress:0.0", "cancel", "start", "progress:0.3", "back"),
            events,
        )
        handler.unregister(callback)
    }

    @Test
    fun `unregister cancels rollback and discards pending gesture callbacks`() = runTest {
        val dispatcher = BackDispatcher()
        val events = mutableListOf<String>()
        val callback = recordingCallback(events)
        val handler = SharedTransitionBackHandler(
            delegate = dispatcher,
            scope = backgroundScope,
            animateCancellation = { event, onProgress ->
                delay(100L)
                onProgress(event.copy(progress = 0F))
            },
            awaitFrame = { delay(16L) },
        )
        handler.register(callback)
        dispatcher.startPredictiveBack(BackEvent())
        dispatcher.progressPredictiveBack(BackEvent(progress = 0.6F))
        dispatcher.cancelPredictiveBack()
        dispatcher.back()
        runCurrent()

        handler.unregister(callback)
        advanceTimeBy(200L)
        runCurrent()

        assertEquals(listOf("start", "progress:0.6"), events)
        assertFalse(handler.isRegistered(callback))
        assertFalse(dispatcher.isEnabled)
        callback.isEnabled = false
        callback.isEnabled = true
        assertFalse(dispatcher.isEnabled)
    }

    @Test
    fun `enabled state and button back remain connected to the original callback`() = runTest {
        val dispatcher = BackDispatcher()
        val events = mutableListOf<String>()
        val callback = recordingCallback(events).apply { isEnabled = false }
        val handler = SharedTransitionBackHandler(delegate = dispatcher, scope = backgroundScope)
        handler.register(callback)

        assertTrue(handler.isRegistered(callback))
        assertFalse(dispatcher.isEnabled)
        callback.isEnabled = true
        assertTrue(dispatcher.isEnabled)
        dispatcher.back()
        runCurrent()
        assertEquals(listOf("back"), events)
        callback.isEnabled = false
        assertFalse(dispatcher.isEnabled)
        handler.unregister(callback)
    }

    private fun recordingCallback(events: MutableList<String>) = object : BackCallback() {
        override fun onBackStarted(backEvent: BackEvent) {
            events += "start"
        }

        override fun onBackProgressed(backEvent: BackEvent) {
            events += "progress:${backEvent.progress}"
        }

        override fun onBackCancelled() {
            events += "cancel"
        }

        override fun onBack() {
            events += "back"
        }
    }
}
