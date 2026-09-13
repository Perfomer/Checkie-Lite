package com.perfomer.checkielite.core.navigation.transition

import androidx.compose.animation.EnterExitState
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SharedImageRendererTest {
    @Test
    fun `opening retains the preview renderer until the transition completes`() {
        assertTrue(shouldDrawSharedImage(true, EnterExitState.Visible))
        assertFalse(shouldDrawSharedImage(true, EnterExitState.PreEnter))
    }

    @Test
    fun `dismissal retains the gallery renderer until the transition completes`() {
        assertTrue(shouldDrawSharedImage(true, EnterExitState.Visible))
        assertFalse(shouldDrawSharedImage(true, EnterExitState.PostExit))
    }

    @Test
    fun `unmatched images are not hidden by another entry visibility`() {
        EnterExitState.entries.forEach { assertTrue(shouldDrawSharedImage(false, it)) }
    }
}
