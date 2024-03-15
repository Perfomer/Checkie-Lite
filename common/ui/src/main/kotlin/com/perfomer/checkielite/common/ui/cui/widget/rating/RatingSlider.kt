@file:Suppress("FunctionName")

package com.perfomer.checkielite.common.ui.cui.widget.rating

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.perfomer.checkielite.common.ui.cui.modifier.conditional
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview
import com.perfomer.checkielite.common.ui.util.dpToPx
import com.perfomer.checkielite.common.ui.util.spToPx
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

private val EMOJI_SIZE by lazy { 26.dpToPx() }
private val TEXT_SIZE by lazy { 14.spToPx() }
private val TEXT_PADDING_TOP by lazy { 25.spToPx() }
private val POINT_SIZE by lazy { 7.dpToPx() }
private val POINT_BORDER_SIZE by lazy { 2.dpToPx() }

private val emojiCoordinates by lazy { RectF() }

private const val DIVISIONS_AMOUNT = 10

@Stable
data class RatingSliderColors(
    val fillLineColor: Color,
    val emptyLineColor: Color,
    val selectedTextColor: Color,
    val unselectedTextColor: Color,
    val pointBackgroundColor: Color,
    val pointSelectedBorderColor: Color = fillLineColor,
    val pointUnselectedBorderColor: Color = emptyLineColor,
    val pointEmojiBorderColor: Color = pointSelectedBorderColor.copy(alpha = 0.5F),
) {
    companion object {

        @Composable
        @ReadOnlyComposable
        fun createByPalette(palette: CuiPalette = LocalCuiPalette.current): RatingSliderColors {
            return RatingSliderColors(
                selectedTextColor = palette.TextAccent,
                unselectedTextColor = palette.TextSecondary,
                pointBackgroundColor = palette.BackgroundPrimary,
                fillLineColor = palette.BackgroundAccentPrimary,
                emptyLineColor = palette.BackgroundSecondary,
            )
        }
    }
}

@Composable
fun RatingSlider(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    colors: RatingSliderColors = RatingSliderColors.createByPalette(),
) {
    val hapticFeedback = LocalHapticFeedback.current

    var offsetX by remember { mutableFloatStateOf(0F) }
    val animatedOffsetX by animateFloatAsState(targetValue = offsetX, label = "OffsetAnimation")

    var width by remember { mutableFloatStateOf(0F) }
    var lastSelectedRating by remember(rating) { mutableIntStateOf(rating) }

    Box(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .onGloballyPositioned { coordinates ->
                width = coordinates.size.width.toFloat()
                offsetX = getOffset(lastSelectedRating, width)
            }
            .draggable(
                enabled = isEnabled,
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offsetX = (offsetX + delta).coerceIn(0F, width)
                    val newRating = getRating(offsetX, width)

                    if (lastSelectedRating != newRating) {
                        lastSelectedRating = newRating
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                },
                onDragStarted = { touch ->
                    offsetX = touch.x.coerceIn(0F, width)
                },
                onDragStopped = {
                    offsetX = getOffset(lastSelectedRating, width)
                    onRatingChange(lastSelectedRating)
                }
            )
            .conditional(isEnabled) {
                pointerInput(Unit) {
                    detectTapGestures { touch ->
                        lastSelectedRating = getRating(touch.x, width)
                        offsetX = getOffset(lastSelectedRating, width)
                        onRatingChange(lastSelectedRating)
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                }
            }
    ) {
        RatingSlideCanvas(
            offsetX = animatedOffsetX,
            lastSelectedRating = lastSelectedRating,
            colors = colors,
        )
    }
}

@Composable
private fun RatingSlideCanvas(
    offsetX: Float,
    lastSelectedRating: Int,
    colors: RatingSliderColors,
) {
    val ratingPaint = remember {
        Paint().apply {
            textSize = TEXT_SIZE
            textAlign = Paint.Align.CENTER
        }
    }

    val context = LocalContext.current
    val reviewReaction = remember(lastSelectedRating) { ReviewReaction.createFromRating(lastSelectedRating) }
    val bitmap = remember(reviewReaction) {
        (AppCompatResources.getDrawable(context, reviewReaction.drawable) as BitmapDrawable).bitmap
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        ProgressLines(
            offsetX = offsetX,
            emptyLineColor = colors.emptyLineColor,
            fillLineColor = colors.fillLineColor,
            pointBackgroundColor = colors.pointBackgroundColor,
            pointSelectedBorderColor = colors.pointSelectedBorderColor,
            pointUnselectedBorderColor = colors.pointUnselectedBorderColor,
        )

        RatingNumbers(
            offsetX = offsetX,
            paint = ratingPaint,
            selectedTextColor = colors.selectedTextColor,
            unselectedTextColor = colors.unselectedTextColor,
        )

        SlidingEmoji(
            emoji = bitmap,
            offsetX = offsetX,
            pointBackgroundColor = colors.pointBackgroundColor,
            pointEmojiBorderColor = colors.pointEmojiBorderColor,
        )
    }
}

private fun DrawScope.ProgressLines(
    offsetX: Float,
    emptyLineColor: Color,
    fillLineColor: Color,
    pointBackgroundColor: Color,
    pointSelectedBorderColor: Color,
    pointUnselectedBorderColor: Color,
) {
    EmptyLine(emptyLineColor)
    FillLine(offsetX, fillLineColor)
    BreakPoints(offsetX, pointBackgroundColor, pointSelectedBorderColor, pointUnselectedBorderColor)
}

private fun DrawScope.EmptyLine(emptyLineColor: Color) {
    val width = size.width
    val height = size.height

    drawLine(
        color = emptyLineColor,
        start = Offset(0F, height / 2),
        end = Offset(width, height / 2),
        strokeWidth = 30F,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.FillLine(
    offsetX: Float,
    fillLineColor: Color,
) {
    val height = size.height

    drawLine(
        color = fillLineColor,
        start = Offset(0F, height / 2),
        end = Offset(offsetX, height / 2),
        strokeWidth = 30F,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.BreakPoints(
    offsetX: Float,
    pointBackgroundColor: Color,
    pointSelectedBorderColor: Color,
    pointUnselectedBorderColor: Color,
) {
    val width = size.width
    val height = size.height

    var breakPoint = 0F
    val onePointLength = width / DIVISIONS_AMOUNT

    repeat(DIVISIONS_AMOUNT + 1) {
        val pointColor =
            if (offsetX < breakPoint) pointUnselectedBorderColor
            else pointSelectedBorderColor

        drawCircle(
            color = pointBackgroundColor,
            radius = POINT_SIZE,
            center = Offset(x = breakPoint, y = height / 2)
        )

        drawCircle(
            color = pointColor,
            radius = POINT_SIZE,
            center = Offset(x = breakPoint, y = height / 2),
            style = Stroke(width = POINT_BORDER_SIZE)
        )

        breakPoint += onePointLength
    }
}

private fun DrawScope.RatingNumbers(
    offsetX: Float,
    paint: Paint,
    selectedTextColor: Color,
    unselectedTextColor: Color,
) {
    val width = size.width
    val height = size.height / 2

    var breakPoint = 0F
    val onePointLength = width / DIVISIONS_AMOUNT

    val paddingTop = TEXT_PADDING_TOP
    val emojiAdditionalPaddingTop = EMOJI_SIZE.toInt() / 2

    for (number in 0..DIVISIONS_AMOUNT) {
        with(drawContext.canvas.nativeCanvas) {
            val distance = offsetX - breakPoint
            val offsetMultiplier = (EMOJI_SIZE - distance.absoluteValue.coerceAtMost(EMOJI_SIZE)) / EMOJI_SIZE
            val interpolatedOffsetMultiplier = (offsetMultiplier * 1.1F).coerceAtMost(1F)

            val offsetY = paddingTop + emojiAdditionalPaddingTop * interpolatedOffsetMultiplier
            val isEmojiNear = distance.absoluteValue <= EMOJI_SIZE / 2
            val currentColor = ColorUtils.blendARGB(unselectedTextColor.toArgb(), selectedTextColor.toArgb(), interpolatedOffsetMultiplier)

            paint.color = currentColor

            paint.isFakeBoldText = isEmojiNear

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

private fun DrawScope.SlidingEmoji(
    emoji: Bitmap,
    offsetX: Float,
    pointBackgroundColor: Color,
    pointEmojiBorderColor: Color,
) {
    val height = size.height

    drawCircle(
        color = pointBackgroundColor,
        radius = 50F,
        center = Offset(x = offsetX, y = height / 2)
    )

    drawCircle(
        color = pointEmojiBorderColor,
        radius = 50F,
        center = Offset(x = offsetX, y = height / 2),
        style = Stroke(width = 8F)
    )

    emojiCoordinates.left = offsetX - EMOJI_SIZE / 2
    emojiCoordinates.top = height / 2 - EMOJI_SIZE / 2
    emojiCoordinates.right = emojiCoordinates.left + EMOJI_SIZE
    emojiCoordinates.bottom = emojiCoordinates.top + EMOJI_SIZE

    drawContext.canvas.nativeCanvas.apply {
        drawBitmap(emoji, null, emojiCoordinates, null)
    }
}

private fun getRating(offsetX: Float, width: Float): Int {
    val adjustedOffset = offsetX.coerceIn(0F, width)
    return (DIVISIONS_AMOUNT * adjustedOffset / width).roundToInt()
}

private fun getOffset(rating: Int, width: Float): Float {
    val offset = width * rating / DIVISIONS_AMOUNT
    return offset.coerceIn(0F, width)
}

@WidgetPreview
@Composable
private fun RatingSliderPreview() {
    CheckieLiteTheme {
        RatingSlider(
            rating = 5,
            onRatingChange = {}
        )
    }
}
