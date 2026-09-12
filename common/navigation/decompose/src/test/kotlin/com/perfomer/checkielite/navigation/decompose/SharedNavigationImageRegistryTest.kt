package com.perfomer.checkielite.navigation.decompose

import com.perfomer.checkielite.core.navigation.transition.SharedNavigationImageRegistry
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SharedNavigationImageRegistryTest {

    @Test
    fun `an endpoint stays enabled before its counterpart is composed`() {
        val registry = SharedNavigationImageRegistry()
        assertFalse(registry.isAmbiguous("photo"))
        registry.register(Any(), "photo", false) { true }
        assertFalse(registry.isAmbiguous("photo"))
        registry.register(Any(), "photo", true) { true }
        assertFalse(registry.isAmbiguous("photo"))
        registry.register(Any(), "photo", true) { true }
        assertTrue(registry.isAmbiguous("photo"))
    }

    @Test
    fun `removing a duplicate enables the remaining pair`() {
        val registry = SharedNavigationImageRegistry()
        registry.register(Any(), "photo", false) { true }
        assertFalse(registry.isAmbiguous("photo"))
        registry.register(Any(), "photo", true) { true }
        assertFalse(registry.isAmbiguous("photo"))

        val duplicate = Any()
        registry.register(duplicate, "photo", false) { true }
        assertTrue(registry.isAmbiguous("photo"))
        registry.unregister(duplicate)
        assertFalse(registry.isAmbiguous("photo"))
    }

    @Test
    fun `page changes and visibility are evaluated without re-registering`() {
        val registry = SharedNavigationImageRegistry()
        var visible = true
        registry.register(Any(), "photo", false) { visible }
        registry.register(Any(), "photo", false) { true }
        registry.register(Any(), "photo", true) { true }
        assertTrue(registry.isAmbiguous("photo"))
        visible = false
        assertFalse(registry.isAmbiguous("photo"))
        visible = true
        assertTrue(registry.isAmbiguous("photo"))
    }

    @Test
    fun `different photos and navigation pairs do not create ambiguity`() {
        val first = SharedNavigationImageRegistry()
        val second = SharedNavigationImageRegistry()
        first.register(Any(), "photo", false) { true }
        first.register(Any(), "other", true) { true }
        second.register(Any(), "photo", true) { true }
        assertFalse(first.isAmbiguous("photo"))
        assertFalse(first.isAmbiguous("other"))
        assertFalse(second.isAmbiguous("photo"))
    }

    @Test
    fun `hidden duplicates do not suppress the visible pair`() {
        val registry = SharedNavigationImageRegistry()
        registry.register(Any(), "photo", false) { true }
        registry.register(Any(), "photo", true) { true }
        registry.register(Any(), "photo", false) { false }
        registry.register(Any(), "photo", true) { false }
        assertFalse(registry.isAmbiguous("photo"))
    }
}
