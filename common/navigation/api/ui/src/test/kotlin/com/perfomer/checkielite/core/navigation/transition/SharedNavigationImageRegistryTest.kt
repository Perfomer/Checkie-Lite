package com.perfomer.checkielite.core.navigation.transition

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SharedNavigationImageRegistryTest {
    @Test
    fun `only the image with a counterpart is matched`() {
        val registry = SharedNavigationImageRegistry()
        registry.register(Any(), "selected", false) { true }
        registry.register(Any(), "neighbor", false) { true }
        val target = Any()
        registry.register(target, "selected", true) { true }

        assertTrue(registry.hasMatch("selected"))
        assertFalse(registry.hasMatch("neighbor"))

        registry.unregister(target)
        assertFalse(registry.hasMatch("selected"))
    }

    @Test
    fun `disabled counterparts do not match`() {
        val registry = SharedNavigationImageRegistry()
        var enabled = false
        registry.register(Any(), "image", false) { true }
        registry.register(Any(), "image", true) { enabled }

        assertFalse(registry.hasMatch("image"))
        enabled = true
        assertTrue(registry.hasMatch("image"))
    }

    @Test
    fun `ambiguous images do not match`() {
        listOf(false, true).forEach { duplicateSide ->
            val registry = SharedNavigationImageRegistry()
            registry.register(Any(), "image", false) { true }
            registry.register(Any(), "image", true) { true }
            registry.register(Any(), "image", duplicateSide) { true }

            assertFalse(registry.hasMatch("image"))
        }
    }
}
