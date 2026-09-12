package com.perfomer.checkielite.core.navigation.transition

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SharedNavigationPairTest {
    @Test
    fun `search pair excludes matching reviews and ungrouped content`() {
        val pair = SharedNavigationPair(setOf(Search))
        assertTrue(content(pair, Search).isTransitionEnabled())
        assertFalse(content(pair, Review).isTransitionEnabled())
        assertFalse(content(pair, DefaultSharedContentGroup).isTransitionEnabled())
    }

    @Test
    fun `matching keys require same pair group and content ID`() {
        val pair = SharedNavigationPair(setOf(Review, Search))
        assertEquals(content(pair, Review).key, content(pair, Review).key)
        assertNotEquals(content(pair, Review).key, content(pair, Search).key)
        assertNotEquals(
            content(pair, Review).key,
            content(SharedNavigationPair(setOf(Review)), Review).key,
        )
        assertNotEquals(content(pair, Review).key, content(pair, Review).key.copy(id = "other"))
    }

    @Test
    fun `local eligibility is still required by an allowed group`() {
        var visible = true
        val content = SharedNavigationContentState("id", Review, SharedNavigationPair(setOf(Review))) { visible }
        assertTrue(content.isTransitionEnabled())
        visible = false
        assertFalse(content.isTransitionEnabled())
    }

    @Test
    fun `content outside a navigation pair does not share`() {
        assertFalse(content(null, Review).isTransitionEnabled())
    }

    private fun content(pair: SharedNavigationPair?, group: SharedContentGroup) =
        SharedNavigationContentState(id = "id", group = group, pair = pair, isEnabled = { true })

    private data object Review : SharedContentGroup
    private data object Search : SharedContentGroup
}
