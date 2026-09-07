package com.perfomer.checkielite.common.ui.presentation.transition

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SharedTransitionVisibilityTest {

    @Test
    fun `incoming element is eligible before the first lazy layout`() {
        assertTrue(
            isSharedTransitionItemEligible(
                totalItemsCount = 0,
                itemOffset = null,
                itemSize = null,
                viewportStartOffset = 0,
                viewportEndOffset = 0,
            ),
        )
    }

    @Test
    fun `measured item inside the viewport is eligible`() {
        assertTrue(isEligible(itemOffset = 100, itemSize = 300))
    }

    @Test
    fun `item touching both viewport edges is eligible`() {
        assertTrue(isEligible(itemOffset = 0, itemSize = 600))
    }

    @Test
    fun `item clipped at the top is not eligible`() {
        assertFalse(isEligible(itemOffset = -1, itemSize = 300))
    }

    @Test
    fun `item clipped at the bottom is not eligible`() {
        assertFalse(isEligible(itemOffset = 301, itemSize = 300))
    }

    @Test
    fun `missing item after layout is not eligible`() {
        assertFalse(isEligible(itemOffset = null, itemSize = null))
    }

    @Test
    fun `item outside the viewport is not eligible`() {
        assertFalse(isEligible(itemOffset = -400, itemSize = 300))
        assertFalse(isEligible(itemOffset = 700, itemSize = 300))
    }

    @Test
    fun `viewport start excludes content hidden beneath a toolbar`() {
        assertFalse(
            isSharedTransitionItemEligible(
                totalItemsCount = 7,
                itemOffset = 40,
                itemSize = 300,
                viewportStartOffset = 56,
                viewportEndOffset = 600,
            ),
        )
    }

    private fun isEligible(itemOffset: Int?, itemSize: Int?): Boolean =
        isSharedTransitionItemEligible(
            totalItemsCount = 7,
            itemOffset = itemOffset,
            itemSize = itemSize,
            viewportStartOffset = 0,
            viewportEndOffset = 600,
        )
}
