package com.perfomer.checkielite.navigation.decompose

import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationImageRegistry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SharedNavigationImageRegistryTest {

    @Test
    fun `each endpoint supplies its own current corner radius`() {
        val registry = SharedNavigationImageRegistry()
        var targetRadius = 0.dp
        registry.register(Any(), "photo", false, { 30.dp }) { true }
        registry.register(Any(), "photo", true, { targetRadius }) { true }
        assertEquals(30.dp, registry.cornerRadius("photo", false))
        assertEquals(0.dp, registry.cornerRadius("photo", true))
        targetRadius = 40.dp
        assertEquals(40.dp, registry.cornerRadius("photo", true))
        assertNull(registry.cornerRadius("other", false))
    }

    @Test
    fun `hidden and ambiguous participants cannot supply a corner radius`() {
        val registry = SharedNavigationImageRegistry()
        registry.register(Any(), "photo", false, { 30.dp }) { true }
        registry.register(Any(), "photo", false, { 60.dp }) { false }
        assertEquals(30.dp, registry.cornerRadius("photo", false))
        val duplicate = Any()
        registry.register(duplicate, "photo", false, { 10.dp }) { true }
        assertNull(registry.cornerRadius("photo", false))
        registry.unregister(duplicate)
        assertEquals(30.dp, registry.cornerRadius("photo", false))
    }

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
