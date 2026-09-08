package com.perfomer.checkielite.navigation.decompose

import com.perfomer.checkielite.core.navigation.transition.SharedNavigationImageRegistry
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SharedNavigationImageRegistryTest {

    @Test
    fun `only one eligible image at each endpoint can match`() {
        val registry = SharedNavigationImageRegistry()
        registry.register(Any(), "photo", false) { true }
        assertFalse(registry.isUnique("photo"))
        registry.register(Any(), "photo", true) { true }
        assertTrue(registry.isUnique("photo"))

        val duplicate = Any()
        registry.register(duplicate, "photo", false) { true }
        assertFalse(registry.isUnique("photo"))
        registry.unregister(duplicate)
        assertTrue(registry.isUnique("photo"))
    }

    @Test
    fun `page changes and visibility are evaluated without re-registering`() {
        val registry = SharedNavigationImageRegistry()
        var visible = true
        registry.register(Any(), "photo", false) { visible }
        registry.register(Any(), "photo", true) { true }
        assertTrue(registry.isUnique("photo"))
        visible = false
        assertFalse(registry.isUnique("photo"))
        visible = true
        assertTrue(registry.isUnique("photo"))
    }

    @Test
    fun `different photos and different navigation pairs never match`() {
        val first = SharedNavigationImageRegistry()
        val second = SharedNavigationImageRegistry()
        first.register(Any(), "photo", false) { true }
        first.register(Any(), "other", true) { true }
        second.register(Any(), "photo", true) { true }
        assertFalse(first.isUnique("photo"))
        assertFalse(first.isUnique("other"))
        assertFalse(second.isUnique("photo"))
    }

    @Test
    fun `hidden duplicates do not suppress the visible pair`() {
        val registry = SharedNavigationImageRegistry()
        registry.register(Any(), "photo", false) { true }
        registry.register(Any(), "photo", true) { true }
        registry.register(Any(), "photo", false) { false }
        registry.register(Any(), "photo", true) { false }
        assertTrue(registry.isUnique("photo"))
    }
}
