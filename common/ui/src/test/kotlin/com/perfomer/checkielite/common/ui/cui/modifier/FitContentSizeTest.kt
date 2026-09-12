package com.perfomer.checkielite.common.ui.cui.modifier

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class FitContentSizeTest {
    private val viewport = IntSize(1080, 2400)

    @Test
    fun `portrait photo excludes vertical letterboxing from its container`() {
        assertEquals(IntSize(1080, 1440), Size(300F, 400F).fitInside(viewport))
    }

    @Test
    fun `landscape and square photos retain their own proportions`() {
        assertEquals(IntSize(1080, 810), Size(400F, 300F).fitInside(viewport))
        assertEquals(IntSize(1080, 1080), Size(300F, 300F).fitInside(viewport))
    }

    @Test
    fun `tall photo is limited by height`() {
        assertEquals(IntSize(800, 2400), Size(100F, 300F).fitInside(viewport))
    }

    @Test
    fun `dismissal scales the photo container with the viewport`() {
        assertEquals(IntSize(864, 1152), Size(300F, 400F).fitInside(IntSize(864, 1920)))
    }

    @Test
    fun `unavailable or invalid dimensions use available bounds`() {
        listOf(Size.Unspecified, Size.Zero, Size(Float.POSITIVE_INFINITY, 1F)).forEach {
            assertEquals(viewport, it.fitInside(viewport))
        }
    }
}
