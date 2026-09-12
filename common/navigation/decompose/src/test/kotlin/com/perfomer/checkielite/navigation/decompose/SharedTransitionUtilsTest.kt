package com.perfomer.checkielite.navigation.decompose

import com.arkivanov.decompose.extensions.compose.stack.animation.Direction
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.NavigationRegistry
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

internal class SharedTransitionUtilsTest {

    @Test
    fun `registered edge animates both sides of forward navigation`() {
        val source = SourceDestination()
        val target = TargetDestination()

        assertTrue(source.hasSharedTransitionWith(target, Direction.EXIT_BACK))
        assertTrue(target.hasSharedTransitionWith(source, Direction.ENTER_FRONT))
    }

    @Test
    fun `registered edge animates both sides of back navigation`() {
        val source = SourceDestination()
        val target = TargetDestination()

        assertTrue(source.hasSharedTransitionWith(target, Direction.ENTER_BACK))
        assertTrue(target.hasSharedTransitionWith(source, Direction.EXIT_FRONT))
    }

    @Test
    fun `registration remains directional`() {
        val source = SourceDestination()
        val target = TargetDestination()

        assertFalse(target.hasSharedTransitionWith(source, Direction.EXIT_BACK))
        assertFalse(source.hasSharedTransitionWith(target, Direction.ENTER_FRONT))
    }

    @Test
    fun `unregistered destinations retain default navigation animation`() {
        val source = SourceDestination()
        val regular = RegularDestination()

        assertFalse(source.hasSharedTransitionWith(regular, Direction.EXIT_BACK))
        assertFalse(regular.hasSharedTransitionWith(source, Direction.ENTER_FRONT))
    }

    private class SourceDestination : Destination()

    private class TargetDestination : Destination()

    private class RegularDestination : Destination()

    companion object {

        @JvmStatic
        @BeforeAll
        fun registerTransition() {
            NavigationRegistry.registerSharedTransition(
                source = SourceDestination::class,
                target = TargetDestination::class,
            )
        }
    }
}
