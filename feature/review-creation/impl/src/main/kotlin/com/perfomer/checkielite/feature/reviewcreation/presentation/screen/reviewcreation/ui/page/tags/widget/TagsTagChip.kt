package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.widget

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.modifier.softShadow
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChip
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChipStyle
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.common.ui.util.resource.text.text
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.TagsPageUiState

@Composable
internal fun TagsTagChip(
    tag: TagsPageUiState.Tag,
    palette: CuiPalette,
    isRecommended: Boolean = false,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hapticFeedback = LocalHapticFeedback.current
    val isAnimatedRecommended = isRecommended && !tag.isSelected
    val recommendedChipBackgroundColor = palette.BackgroundAccentTertiary.copy(alpha = 0.38F)
    val recommendedChipIconBackgroundColor = palette.BackgroundAccentTertiary.copy(alpha = 0.52F)
    val recommendedChipBorderColors = listOf(
        palette.OutlineAccentPrimary.copy(alpha = 0.68F),
        palette.OutlineAccentSecondary.copy(alpha = 0.52F),
    )
    val recommendedDashProgress = rememberInfiniteTransition(label = "recommendedChipBorder")
        .animateFloat(
            initialValue = 0F,
            targetValue = 1F,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = if (isSystemInDarkTheme()) 7500 else 4500,
                    easing = LinearEasing,
                )
            ),
            label = "recommendedChipBorderProgress",
        )

    val chipStyle = when {
        tag.isSelected -> CuiChipStyle(
            iconBackgroundColor = palette.BackgroundAccentSecondary,
            textBackgroundColor = palette.BackgroundAccentTertiary,
            borderColor = palette.OutlineAccentPrimary,
            borderWidth = 1.5.dp,
            fontWeight = FontWeight.Medium,
        )
        isRecommended -> CuiChipStyle(
            iconBackgroundColor = recommendedChipIconBackgroundColor,
            textBackgroundColor = recommendedChipBackgroundColor,
            borderColor = Color.Transparent,
            borderWidth = 1.dp,
            fontWeight = FontWeight.Medium,
        )
        else -> CuiChipStyle.elevated(palette)
    }

    val textColor = when {
        tag.isSelected -> palette.TextAccent
        isRecommended -> palette.TextPrimary
        else -> palette.TextPrimary
    }

    CuiChip(
        onClick = { onClick(tag.id) },
        onLongClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            onLongClick(tag.id)
        },
        style = chipStyle,
        interactionSource = interactionSource,
        modifier = modifier.softShadow(
            interactionSource = interactionSource,
            shape = CircleShape,
            radius = 8.dp,
            offset = DpOffset(x = 0.dp, y = 2.dp),
        ).recommendedAnimatedBorder(
            isEnabled = isAnimatedRecommended,
            progress = recommendedDashProgress.value,
            colors = recommendedChipBorderColors,
        ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (tag.emoji != null) {
                Text(text = tag.emoji, color = textColor)
                CuiSpacer(4.dp)
            }

            Box {
                Text(
                    text = text(tag.value),
                    color = textColor,
                )

                // Crutch to keep size reserved for medium font weight
                // to avoid text flickering after chip become selected
                Text(
                    text = text(tag.value),
                    color = Color.Transparent,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

private fun Modifier.recommendedAnimatedBorder(
    isEnabled: Boolean,
    progress: Float,
    colors: List<Color>,
): Modifier {
    if (!isEnabled) return this

    return drawWithCache {
        val outline = CircleShape.createOutline(size, layoutDirection, this)
        val path = when (outline) {
            is Outline.Generic -> outline.path
            is Outline.Rounded -> Path().apply { addRoundRect(outline.roundRect) }
            is Outline.Rectangle -> Path().apply { addRect(outline.rect) }
        }
        val dashLength = 12.dp.toPx()
        val gapLength = 10.dp.toPx()
        val patternLength = dashLength + gapLength
        val dashPathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashLength, gapLength),
            phase = -patternLength * progress,
        )
        val borderStrokeWidth = 1.25.dp.toPx()
        val borderBrush = Brush.linearGradient(colors = colors)

        onDrawWithContent {
            drawContent()
            drawPath(
                path = path,
                brush = borderBrush,
                style = Stroke(
                    width = borderStrokeWidth,
                    pathEffect = dashPathEffect,
                )
            )
        }
    }
}
