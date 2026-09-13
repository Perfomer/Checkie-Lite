package com.perfomer.checkielite.core.navigation.transition

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SharedContentRoleTest {
    @Test
    fun `return to list matches page but never its recommendations with the same ID`() {
        val pair = pair(ListItem, Page)
        val listItem = content(pair, ListItem, isSource = true)
        val page = content(pair, Page, isSource = false)
        val recommendation = content(pair, Recommendation, isSource = false)

        assertTrue(listItem.isTransitionEnabled())
        assertTrue(page.isTransitionEnabled())
        assertEquals(listItem.key, page.key)
        assertFalse(recommendation.isTransitionEnabled())
        assertNotEquals(listItem.key, recommendation.key)
    }

    @Test
    fun `nested details share recommendation with page without matching both recommendation carousels`() {
        val pair = pair(Recommendation, Page)
        val recommendation = content(pair, Recommendation, isSource = true)
        val page = content(pair, Page, isSource = false)
        assertTrue(recommendation.isTransitionEnabled())
        assertTrue(page.isTransitionEnabled())
        assertEquals(recommendation.key, page.key)
        assertFalse(content(pair, Page, isSource = true).isTransitionEnabled())
        assertFalse(content(pair, Recommendation, isSource = false).isTransitionEnabled())
        assertNotEquals(recommendation.key, page.key.copy(id = "another review"))
    }

    @Test
    fun `different channels in the same group cannot match each other`() {
        val pair = SharedNavigationPair(
            SharedTransitionPolicy(setOf(SharedContentMatch(ListItem, Page), SharedContentMatch(Recommendation, Recommendation))),
        )
        assertNotEquals(
            content(pair, ListItem, isSource = true).key,
            content(pair, Recommendation, isSource = false).key,
        )
    }

    @Test
    fun `group shorthand matches default roles only`() {
        val pair = SharedNavigationPair(setOf(Review))
        assertFalse(content(pair, Recommendation, isSource = true).isTransitionEnabled())
        val source = content(pair, SharedContentRole.Default(Review), isSource = true)
        val target = content(pair, SharedContentRole.Default(Review), isSource = false)
        assertTrue(source.isTransitionEnabled())
        assertTrue(target.isTransitionEnabled())
        assertEquals(source.key, target.key)
    }

    @Test
    fun `ambiguous mappings and mismatched groups fail at registration`() {
        assertThrows(IllegalArgumentException::class.java) {
            SharedTransitionPolicy(setOf(SharedContentMatch(ListItem, Page), SharedContentMatch(ListItem, Recommendation)))
        }
        assertThrows(IllegalArgumentException::class.java) {
            SharedTransitionPolicy(setOf(SharedContentMatch(ListItem, Page), SharedContentMatch(Recommendation, Page)))
        }
        assertThrows(IllegalArgumentException::class.java) {
            SharedContentMatch(ListItem, SharedContentRole.Default(SharedContentGroup.Default))
        }
    }

    private fun pair(source: SharedContentRole, target: SharedContentRole) =
        SharedNavigationPair(SharedTransitionPolicy(setOf(SharedContentMatch(source, target))))

    private fun content(pair: SharedNavigationPair, role: SharedContentRole, isSource: Boolean) =
        SharedNavigationContentState(
            id = "same review",
            group = Review,
            pair = pair,
            role = role,
            isSource = isSource,
            isEnabled = { true },
        )

    private data object Review : SharedContentGroup
    private data object ListItem : SharedContentRole { override val group = Review }
    private data object Page : SharedContentRole { override val group = Review }
    private data object Recommendation : SharedContentRole { override val group = Review }
}
