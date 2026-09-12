package com.perfomer.checkielite.core.navigation.transition

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ImageClipShapeTest {

    @Test
    fun `corners clip the entire animated container without scaling the radius`() {
        val shape = ImageClipShape(30.dp)
        val outline = shape.createOutline(Size(200F, 600F), LayoutDirection.Ltr, Density(1F)) as Outline.Rounded
        assertEquals(RoundRect(Rect(0F, 0F, 200F, 600F), CornerRadius(30F)), outline.roundRect)
    }

    @Test
    fun `target content clips to shared bounds using display density`() {
        val shape = ImageClipShape(30.dp)
        val outline = shape.createOutline(Size(200F, 200F), LayoutDirection.Rtl, Density(2F)) as Outline.Rounded
        assertEquals(RoundRect(Rect(0F, 0F, 200F, 200F), CornerRadius(60F)), outline.roundRect)
    }

    @Test
    fun `small containers limit oversized corners`() {
        val shape = ImageClipShape(30.dp)
        val outline = shape.createOutline(Size(20F, 40F), LayoutDirection.Ltr, Density(1F)) as Outline.Rounded
        assertEquals(RoundRect(Rect(0F, 0F, 20F, 40F), CornerRadius(10F)), outline.roundRect)
    }
}
