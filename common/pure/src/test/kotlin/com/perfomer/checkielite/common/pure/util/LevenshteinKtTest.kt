package com.perfomer.checkielite.common.pure.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LevenshteinKtTest {

    @Test
    fun `test levenshtein distance`() {
        testDistance("", "", 0)
        testDistance("1", "1", 0)
        testDistance("1", "2", 1)
        testDistance("12", "12", 0)
        testDistance("123", "12", 1)
        testDistance("1234", "1", 3)
        testDistance("1234", "1233", 1)
        testDistance("", "12345", 5)
        testDistance("kitten", "mittens", 2)
        testDistance("canada", "canad", 1)
        testDistance("canad", "canada", 1)
    }

    private fun testDistance(a: String, b: String, expectedDistance: Int) {
        val d = levenshtein(a, b)
        assertEquals(expectedDistance, d, "Distance did not match for `$a` and `$b`")
    }
}