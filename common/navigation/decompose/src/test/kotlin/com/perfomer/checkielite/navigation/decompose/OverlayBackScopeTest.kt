package com.perfomer.checkielite.navigation.decompose

import androidx.navigationevent.NavigationEvent
import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventHandler
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.NavigationEventInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/** Dispatch contract used by OverlayNavigation's main-content dispatcher owner. */
class OverlayBackScopeTest {

    private val root = NavigationEventDispatcher()
    private val main = NavigationEventDispatcher(parent = root)
    private val input = BackInput().also(root::addInput)
    private val overlay = RecordingHandler().also(root::addHandler)
    // Screens enter the composition after the host's persistent overlay handler.
    private val editor = RecordingHandler().also(main::addHandler)

    @Test
    fun `overlay receives predictive gesture instead of later registered editor`() {
        main.isEnabled = false

        input.start()
        input.progress()
        input.complete()

        assertEquals(listOf("start", "progress", "complete"), overlay.events)
        assertEquals(emptyList<String>(), editor.events)
    }

    @Test
    fun `cancelled gesture and repeated back stay with displayed overlay`() {
        main.isEnabled = false

        input.start()
        input.progress()
        input.cancel()
        input.complete()

        assertEquals(listOf("start", "progress", "cancel", "complete"), overlay.events)
        assertEquals(emptyList<String>(), editor.events)
    }

    @Test
    fun `editor handler is restored only after overlay stops being displayed`() {
        main.isEnabled = false
        input.complete()
        // A retained closing overlay still consumes the next Back.
        input.complete()
        assertEquals(emptyList<String>(), editor.events)

        overlay.isBackEnabled = false
        main.isEnabled = true
        input.complete()

        assertEquals(listOf("complete", "complete"), overlay.events)
        assertEquals(listOf("complete"), editor.events)
    }

    @Test
    fun `handlers added or reenabled beneath an open overlay cannot steal back`() {
        main.isEnabled = false
        val nested = NavigationEventDispatcher(parent = main)
        val lateHandler = RecordingHandler().also(nested::addHandler)
        editor.isBackEnabled = false
        editor.isBackEnabled = true

        input.complete()

        assertEquals(listOf("complete"), overlay.events)
        assertEquals(emptyList<String>(), editor.events)
        assertEquals(emptyList<String>(), lateHandler.events)
    }

    private class BackInput : NavigationEventInput() {
        fun start() = dispatchOnBackStarted(NavigationEvent())
        fun progress() = dispatchOnBackProgressed(NavigationEvent(progress = 0.5F))
        fun complete() = dispatchOnBackCompleted()
        fun cancel() = dispatchOnBackCancelled()
    }

    private class RecordingHandler : NavigationEventHandler<NavigationEventInfo>(
        initialInfo = NavigationEventInfo.None,
        isBackEnabled = true,
    ) {
        val events = mutableListOf<String>()

        override fun onBackStarted(event: NavigationEvent) { events += "start" }
        override fun onBackProgressed(event: NavigationEvent) { events += "progress" }
        override fun onBackCompleted() { events += "complete" }
        override fun onBackCancelled() { events += "cancel" }
    }
}
