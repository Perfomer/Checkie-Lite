@file:Suppress("FunctionName")

package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewinfo.widget

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.common.ui.theme.PreviewTheme
import com.perfomer.checkielite.common.ui.theme.WidgetPreview
import com.perfomer.checkielite.core.entity.ReviewReaction
import kotlin.math.roundToInt

private const val EMOJI_SIZE = 52F
private const val DIVISIONS_AMOUNT = 10

@Composable
internal fun RatingSlider(
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    var offsetX by remember { mutableStateOf(0F) }
    var width by remember { mutableStateOf(0F) }
    var lastSelectedRating by remember { mutableStateOf(rating) }

    UpdateEffect(lastSelectedRating) {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    Box(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .onGloballyPositioned { coordinates ->
                width = coordinates.size.width.toFloat()
                offsetX = getOffset(lastSelectedRating, width)
            }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offsetX = adjustOffset(offsetX + delta, width)
                    lastSelectedRating = getRating(offsetX, width)
                },
                onDragStarted = { touch ->
                    offsetX = adjustOffset(touch.x, width)
                },
                onDragStopped = {
                    offsetX = getOffset(lastSelectedRating, width)
                    onRatingChange(lastSelectedRating)
                }
            )
            .pointerInput(Unit) {
                detectTapGestures { touch ->
                    lastSelectedRating = getRating(touch.x, width)
                    offsetX = getOffset(lastSelectedRating, width)
                    onRatingChange(lastSelectedRating)
                }
            }
    ) {
        RatingSlideCanvas(
            offsetX = offsetX,
            lastSelectedRating = lastSelectedRating,
        )
    }
}

@Composable
private fun RatingSlideCanvas(
    offsetX: Float,
    lastSelectedRating: Int,
) {
    val ratingPaint = remember {
        Paint().apply {
            textSize = EMOJI_SIZE
            textAlign = Paint.Align.CENTER
        }
    }

    val emojiPaint = remember {
        Paint().apply {
            textSize = EMOJI_SIZE
            textAlign = Paint.Align.CENTER
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        ProgressLines(offsetX)
        RatingNumbers(offsetX, ratingPaint)

        SlidingEmoji(
            emoji = ReviewReaction.createFromRating(lastSelectedRating).emoji,
            offsetX = offsetX,
            paint = emojiPaint
        )
    }
}

private fun DrawScope.ProgressLines(offsetX: Float) {
    EmptyLine()
    FillLine(offsetX)
    BreakPoints(offsetX)
}

private fun DrawScope.EmptyLine() {
    val width = size.width
    val height = size.height

    drawLine(
        color = CuiPalette.Light.BackgroundSecondary,
        start = Offset(0F, height / 2),
        end = Offset(width, height / 2),
        strokeWidth = 30F,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.FillLine(offsetX: Float) {
    val height = size.height

    drawLine(
        color = CuiPalette.Light.BackgroundAccentPrimary,
        start = Offset(0F, height / 2),
        end = Offset(offsetX, height / 2),
        strokeWidth = 30F,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.BreakPoints(offsetX: Float) {
    val width = size.width
    val height = size.height

    var breakPoint = 0F
    val onePointLength = width / DIVISIONS_AMOUNT

    repeat(DIVISIONS_AMOUNT + 1) {
        val pointColor =
            if (offsetX < breakPoint) CuiPalette.Light.BackgroundSecondary
            else CuiPalette.Light.BackgroundAccentPrimary

        drawCircle(
            color = CuiPalette.Light.BackgroundPrimary,
            radius = 20F,
            center = Offset(x = breakPoint, y = height / 2)
        )

        drawCircle(
            color = pointColor,
            radius = 20F,
            center = Offset(x = breakPoint, y = height / 2),
            style = Stroke(width = 5F)
        )

        breakPoint += onePointLength
    }
}

private fun DrawScope.RatingNumbers(offsetX: Float, paint: Paint) {
    val width = size.width
    val height = size.height / 2

    var breakPoint = 0F
    val onePointLength = width / DIVISIONS_AMOUNT

    val paddingTop = 75
    val emojiPaddingTop = EMOJI_SIZE.toInt() / 2 + paddingTop
    val offsetRange = (-EMOJI_SIZE)..EMOJI_SIZE

    for (number in 0..DIVISIONS_AMOUNT) {
        drawContext.canvas.nativeCanvas.apply {
            val isEmojiNear = offsetX - breakPoint in offsetRange

            val offsetY =
                if (isEmojiNear) emojiPaddingTop
                else paddingTop

            paint.apply {
                color =
                    if (isEmojiNear) CuiPalette.Light.TextAccent.toArgb()
                    else CuiPalette.Light.TextSecondary.toArgb()
            }

            drawText(
                number.toString(),
                breakPoint,
                height + offsetY,
                paint
            )
        }

        breakPoint += onePointLength
    }
}

private fun DrawScope.SlidingEmoji(emoji: String, offsetX: Float, paint: Paint) {
    val height = size.height

    drawCircle(
        color = CuiPalette.Light.BackgroundPrimary,
        radius = 50F,
        center = Offset(x = offsetX, y = height / 2)
    )

    drawCircle(
        color = CuiPalette.Light.BackgroundAccentPrimary,
        radius = 50F,
        center = Offset(x = offsetX, y = height / 2),
        style = Stroke(width = 8F)
    )

    drawContext.canvas.nativeCanvas.apply {
        drawText(
            emoji,
            offsetX,
            height / 2 + 15,
            paint
        )
    }
}

private fun getRating(offsetX: Float, width: Float): Int {
    val adjustedOffset = adjustOffset(offsetX, width)
    return (DIVISIONS_AMOUNT * adjustedOffset / width).roundToInt()
}

private fun getOffset(rating: Int, width: Float): Float {
    val offset = width * rating / DIVISIONS_AMOUNT
    return adjustOffset(offset, width)
}

private fun adjustOffset(offsetX: Float, width: Float): Float {
    return when {
        offsetX < 0 -> 0F
        offsetX > width -> width
        else -> offsetX
    }
}

@WidgetPreview
@Composable
private fun RatingSliderPreview() {
    PreviewTheme {
        RatingSlider(
            rating = 5,
            onRatingChange = {}
        )
    }
}
