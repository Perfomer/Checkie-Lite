package com.perfomer.checkielite.core.navigation.transition

import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.NavigationRegistry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class SharedTransitionPolicyTest {
    @Test
    fun `edge policy is explicit directional and copied at registration`() {
        val groups = mutableSetOf<SharedContentGroup>(Review)
        NavigationRegistry.registerSharedTransition(Source::class, Target::class, groups)
        groups.clear()
        assertEquals(setOf(Review), NavigationRegistry.sharedTransitionGroups(Source, Target))
        assertEquals(emptySet<SharedContentGroup>(), NavigationRegistry.sharedTransitionGroups(Target, Source))
    }

    @Test
    fun `empty policy cannot silently enable a transition`() {
        assertThrows(IllegalArgumentException::class.java) {
            NavigationRegistry.registerSharedTransition(Target::class, Source::class, emptySet())
        }
    }

    private data object Source : Destination()
    private data object Target : Destination()
    private data object Review : SharedContentGroup
}
