package com.perfomer.checkielite.core.navigation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class NavigationDataTest {

    @Test
    fun `values retain their key types`() {
        val name = NavigationDataKey<String>()
        val count = NavigationDataKey<Int>()
        val data = navigationData {
            put(name, "Checkie")
            put(count, 10)
        }

        assertEquals("Checkie", data[name])
        assertEquals(10, data[count])
    }

    @Test
    fun `keys with the same value type remain independent`() {
        val first = NavigationDataKey<String>()
        val second = NavigationDataKey<String>()
        val data = navigationData { put(first, "first") }

        assertEquals("first", data[first])
        assertNull(data[second])
    }

    @Test
    fun `empty navigation data is reusable`() {
        assertTrue(NavigationData.Empty.isEmpty)
        assertTrue(navigationData {}.isEmpty)
    }
}
