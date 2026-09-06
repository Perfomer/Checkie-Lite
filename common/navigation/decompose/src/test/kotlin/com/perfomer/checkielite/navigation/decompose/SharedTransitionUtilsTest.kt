package com.perfomer.checkielite.navigation.decompose

import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.SharedTransitionDestination
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SharedTransitionUtilsTest {

    @Test
    fun `destinations in the same group share a transition in both directions`() {
        val list = SharedDestination("review")
        val details = SharedDestination("review")

        assertTrue(list.hasSharedTransitionWith(details))
        assertTrue(details.hasSharedTransitionWith(list))
    }

    @Test
    fun `different groups retain the default navigation animation`() {
        assertFalse(SharedDestination("review").hasSharedTransitionWith(SharedDestination("gallery")))
    }

    @Test
    fun `both destinations must opt in to shared transitions`() {
        val shared = SharedDestination("review")
        val regular = RegularDestination()

        assertFalse(shared.hasSharedTransitionWith(regular))
        assertFalse(regular.hasSharedTransitionWith(shared))
        assertFalse(regular.hasSharedTransitionWith(RegularDestination()))
    }

    private class SharedDestination(
        override val sharedTransitionGroup: String,
    ) : Destination(), SharedTransitionDestination

    private class RegularDestination : Destination()
}
