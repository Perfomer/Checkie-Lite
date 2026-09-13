package com.perfomer.checkielite.common.ui.cui.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import kotlin.math.roundToInt

/** Fits a child's actual layout to its content aspect ratio, centered in the available space. */
fun Modifier.fitContentSize(contentSize: () -> Size): Modifier = layout { measurable, constraints ->
    if (!constraints.hasBoundedWidth || !constraints.hasBoundedHeight) {
        val child = measurable.measure(constraints)
        layout(child.width, child.height) { child.placeRelative(0, 0) }
    } else {
        val fitted = contentSize().fitInside(IntSize(constraints.maxWidth, constraints.maxHeight))
        val child = measurable.measure(Constraints.fixed(fitted.width, fitted.height))
        val width = constraints.constrainWidth(child.width)
        val height = constraints.constrainHeight(child.height)
        layout(width, height) { child.placeRelative((width - child.width) / 2, (height - child.height) / 2) }
    }
}

internal fun Size.fitInside(bounds: IntSize): IntSize {
    if (!isSpecified || !width.isFinite() || !height.isFinite() || width <= 0F || height <= 0F) return bounds
    val scale = minOf(bounds.width / width, bounds.height / height)
    return IntSize((width * scale).roundToInt(), (height * scale).roundToInt())
}
