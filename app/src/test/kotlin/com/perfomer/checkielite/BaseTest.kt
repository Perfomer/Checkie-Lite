package com.perfomer.checkielite

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class BaseTest {

    @Test
    fun `GIVEN true WHEN checking true THEN it should be true`() {
        // GIVEN
        val input = true

        // WHEN
        val result = input == true

        // THEN
        assertEquals(result, input)
    }
}