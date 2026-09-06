package com.perfomer.checkielite.feature.main.presentation.screen.main.ui.widget

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.modifier.softShadow
import com.perfomer.checkielite.common.ui.cui.widget.cell.CuiReviewHorizontalItem
import com.perfomer.checkielite.common.ui.cui.widget.cell.ReviewItem
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview

private val ReviewCardShape = RoundedCornerShape(24.dp)

@Composable
internal fun MainReviewCard(
    item: ReviewItem,
    onClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = ReviewCardShape,
        color = LocalCuiPalette.current.BackgroundElevationBase,
        modifier = modifier.softShadow(shape = ReviewCardShape)
    ) {
        Box {
            if (item.rating == 10) {
                DiamondGlow(modifier = Modifier.matchParentSize())
            }

            CuiReviewHorizontalItem(
                item = item,
                onClick = onClick,
                contentPadding = PaddingValues(start = 8.dp, top = 8.dp, end = 12.dp, bottom = 8.dp),
                imageCornerRadius = 16.dp,
                imageSize = 56.dp,
                imageRightOffset = 12.dp,
            )
        }
    }
}

@Composable
private fun DiamondGlow(modifier: Modifier = Modifier) {
    val isDark = LocalCuiPalette.current.BackgroundElevationBase.luminance() < 0.5F
    val transition = rememberInfiniteTransition(label = "Diamond glow")
    val glow = transition.animateFloat(
        initialValue = 0F,
        targetValue = 1F,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "Diamond light breathing",
    )

    Box(
        modifier = modifier.drawWithCache {
            // Keep the light around the rating even on wide cards and at large font scales.
            val glowWidth = minOf(164.dp.toPx(), size.width * 0.58F)
            val sky = Color(0xFF63CDFF)
            val blue = Color(0xFF448AFF)
            val ice = Color(0xFFBCF6FF)
            val skyGlow = Brush.radialGradient(
                0F to sky.copy(alpha = if (isDark) 0.30F else 0.44F),
                0.45F to sky.copy(alpha = if (isDark) 0.18F else 0.24F),
                1F to sky.copy(alpha = 0F),
                center = Offset(size.width, size.height * 0.12F),
                radius = glowWidth,
            )
            val blueGlow = Brush.radialGradient(
                0F to blue.copy(alpha = if (isDark) 0.34F else 0.32F),
                0.4F to blue.copy(alpha = if (isDark) 0.18F else 0.16F),
                1F to blue.copy(alpha = 0F),
                center = Offset(size.width - 16.dp.toPx(), size.height * 1.1F),
                radius = glowWidth * 0.78F,
            )
            val iceGlow = Brush.radialGradient(
                colors = listOf(ice.copy(alpha = if (isDark) 0.16F else 0.60F), ice.copy(alpha = 0F)),
                center = Offset(size.width - 24.dp.toPx(), size.height * 0.32F),
                radius = glowWidth * 0.48F,
            )
            val facet = Path().apply {
                moveTo(size.width - glowWidth * 0.72F, 0F)
                lineTo(size.width - glowWidth * 0.4F, 0F)
                lineTo(size.width - glowWidth * 0.05F, size.height)
                lineTo(size.width - glowWidth * 0.15F, size.height)
                close()
            }
            val facetLight = Brush.linearGradient(
                colors = listOf(Color.White.copy(alpha = 0F), ice.copy(alpha = if (isDark) 0.08F else 0.24F)),
                start = Offset(size.width - glowWidth * 0.6F, 0F),
                end = Offset(size.width, size.height),
            )

            onDrawBehind {
                // Read animation state only while drawing: text and layout stay untouched.
                val progress = glow.value
                drawRect(brush = skyGlow)
                drawRect(brush = blueGlow, alpha = 0.78F + progress * 0.22F)
                drawRect(brush = iceGlow, alpha = 0.55F + progress * 0.45F)
                drawPath(path = facet, brush = facetLight, alpha = 0.5F + progress * 0.5F)
            }
        }
    )
}

@ScreenPreview
@Composable
private fun MainReviewCardLightPreview() = MainReviewCardPreview(darkTheme = false)

@ScreenPreview
@Composable
private fun MainReviewCardDarkPreview() = MainReviewCardPreview(darkTheme = true)

@Composable
private fun MainReviewCardPreview(darkTheme: Boolean) = CheckieLiteTheme(darkTheme = darkTheme) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .background(LocalCuiPalette.current.BackgroundAccentTertiary)
            .padding(20.dp)
    ) {
        listOf(10, 9, 10, 0).forEachIndexed { index, rating ->
            MainReviewCard(
                item = ReviewItem(
                    id = index.toString(),
                    title = "Strawberry Kiwi — a very long product name",
                    brand = if (index < 2) "Plonq" else null,
                    imageUri = null,
                    rating = rating,
                    isSyncing = false,
                ),
                onClick = {},
            )
        }
    }
}
