package com.perfomer.checkielite.navigation.decompose

import com.perfomer.checkielite.core.navigation.transition.SharedContentGroup
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SharedNavigationPairsTest {
    @Test
    fun `both endpoints obtain the same pair before their first composition`() {
        val pairs = SharedNavigationPairs()
        val pair = pairs.select("main", "search", setOf(Search))
        assertSame(pair, pairs.forEntry("main"))
        assertSame(pair, pairs.forEntry("search"))
        assertSame(pair, pairs.select("main", "search", setOf(Search)))
        assertTrue(pair.allows(Search))
        assertFalse(pair.allows(Review))
    }

    @Test
    fun `back and cancelled gestures reuse the same ordered pair`() {
        val pairs = SharedNavigationPairs()
        val pair = pairs.select("main", "search", setOf(Search))
        pairs.forget("main") // The source leaves composition after opening Search.
        assertSame(pair, pairs.select("main", "search", setOf(Search)))
        assertSame(pair, pairs.select("main", "search", setOf(Search)))
    }

    @Test
    fun `queued transition cannot change a captured policy`() {
        val pairs = SharedNavigationPairs()
        val running = pairs.select("main", "search", setOf(Search))
        val queued = pairs.select("search", "details", setOf(Review))
        assertNotSame(running, queued)
        assertTrue(running.allows(Search))
        assertFalse(running.allows(Review))
        assertSame(queued, pairs.forEntry("search"))
    }

    @Test
    fun `separate entries and unregistered edges cannot share`() {
        val pairs = SharedNavigationPairs()
        val first = pairs.select("details-1", "details-2", setOf(Review))
        val second = pairs.select("details-2", "details-3", setOf(Review))
        assertNotSame(first, second)
        val disabled = pairs.select("details-3", "settings", emptySet())
        assertFalse(disabled.allows(Review))
        pairs.forget("settings")
        assertNull(pairs.forEntry("settings"))
    }

    private data object Search : SharedContentGroup
    private data object Review : SharedContentGroup
}
