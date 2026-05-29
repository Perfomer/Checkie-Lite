package com.perfomer.checkielite.feature.appfunctions.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RatingCoercionTest {

    @Test
    fun `coerces rating into 0 to 10`() {
        assertEquals(0, coerceRating(-3))
        assertEquals(0, coerceRating(0))
        assertEquals(7, coerceRating(7))
        assertEquals(10, coerceRating(10))
        assertEquals(10, coerceRating(15))
    }
}
