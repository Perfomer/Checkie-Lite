package com.perfomer.checkielite.feature.main.presentation.screen.main.ui.widget

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.modifier.SharedContentKey
import com.perfomer.checkielite.common.ui.cui.modifier.SharedContentPart
import com.perfomer.checkielite.common.ui.cui.modifier.sharedNavigationContent
import com.perfomer.checkielite.common.ui.cui.modifier.softShadow
import com.perfomer.checkielite.common.ui.cui.widget.cell.CuiReviewHorizontalItem
import com.perfomer.checkielite.common.ui.cui.widget.cell.ReviewItem
import com.perfomer.checkielite.common.ui.cui.widget.rating.ReviewRating
import com.perfomer.checkielite.common.ui.presentation.transition.SharedImage
import com.perfomer.checkielite.common.ui.presentation.transition.SharedNavigationContainer
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val ReviewCardShape = RoundedCornerShape(24.dp)

@Composable
internal fun MainReviewCard(
    item: ReviewItem,
    onClick: (id: String) -> Unit,
    isTransitionEnabled: () -> Boolean = { true },
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    SharedNavigationContainer(
        contentId = item.id,
        cornerRadius = 24.dp,
        otherCornerRadius = 0.dp,
        color = LocalCuiPalette.current.BackgroundElevationBase,
        otherColor = LocalCuiPalette.current.BackgroundPrimary,
        isEnabled = isTransitionEnabled,
        modifier = modifier.softShadow(interactionSource = interactionSource, shape = ReviewCardShape)
    ) {
        Surface(shape = ReviewCardShape, color = Color.Transparent) {
            Box {
                if (item.rating == 10) {
                    DiamondGlow(modifier = Modifier.matchParentSize())
                }

                CuiReviewHorizontalItem(
                    item = item,
                    onClick = onClick,
                    interactionSource = interactionSource,
                    contentPadding = PaddingValues(start = 8.dp, top = 8.dp, end = 12.dp, bottom = 8.dp),
                    imageCornerRadius = 16.dp,
                    imageSize = 56.dp,
                    imageRightOffset = 12.dp,
                    titleModifier = Modifier.sharedNavigationContent(
                        key = SharedContentKey(item.id, SharedContentPart.Title),
                        isEnabled = isTransitionEnabled,
                    ),
                    brandModifier = Modifier.sharedNavigationContent(
                        key = SharedContentKey(item.id, SharedContentPart.Subtitle),
                        isEnabled = isTransitionEnabled,
                    ),
                    ratingContent = if (item.rating == 10) {
                        {
                            ReviewRating(
                                rating = item.rating,
                                reactionContent = { FloatingDiamond() },
                            )
                        }
                    } else {
                        null
                    },
                    imageContent = item.imageUri?.let { uri ->
                        {
                            SharedImage(
                                contentId = item.id,
                                imageUri = uri,
                                cornerRadius = 16.dp,
                                otherCornerRadius = 24.dp,
                                isTransitionEnabled = isTransitionEnabled,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        width = 1.dp,
                                        color = LocalCuiPalette.current.OutlinePicture,
                                        shape = RoundedCornerShape(16.dp),
                                    )
                            )
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun DiamondGlow(modifier: Modifier = Modifier) {
    val isDark = LocalCuiPalette.current.BackgroundElevationBase.luminance() < 0.5F
    val transition = rememberInfiniteTransition(label = "Diamond mesh")
    val phase = transition.animateFloat(
        initialValue = 0F,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3250, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Diamond mesh drift",
    )

    Box(
        modifier = modifier
            // Isolate the mask from the card surface, text, and image underneath.
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithCache {
                // Keep the light around the rating even on wide cards and at large font scales.
                val glowWidth = minOf(92.dp.toPx(), size.width * 0.3F)
                // A longer fade softens the left edge inward without extending the mesh.
                val fadeWidth = minOf(56.dp.toPx(), glowWidth)
                val fadeStops = Array(17) { index ->
                    val position = index / 16F
                    val opacity = position * position * (3F - 2F * position)
                    position to Color.Black.copy(alpha = opacity)
                }
                val leftFade = Brush.horizontalGradient(
                    *fadeStops,
                    startX = size.width - glowWidth,
                    endX = size.width - glowWidth + fadeWidth,
                )
                // Unit gradients are cached, then stretched into overlapping, moving mesh lobes.
                val sky = meshBrush(Color(0xFF79CEFF), alpha = if (isDark) 0.22F else 0.48F)
                val azure = meshBrush(Color(0xFF397DF6), alpha = if (isDark) 0.40F else 0.66F)
                val periwinkle = meshBrush(Color(0xFF929BFF), alpha = if (isDark) 0.25F else 0.48F)
                val cyan = meshBrush(Color(0xFF41E3EB), alpha = if (isDark) 0.30F else 0.62F)
                val pearl = meshBrush(Color(0xFFE9FAFF), alpha = if (isDark) 0.06F else 0.88F)
                // The reaction is 28 dp wide with 12 dp of trailing card padding.
                val diamondCenter = Offset(size.width - 26.dp.toPx(), size.height / 2F)
                val haloColor = Color(0xFFF4FCFF)
                val haloOpacity = if (isDark) 0.58F else 0.98F
                val diamondHalo = Brush.radialGradient(
                    0F to haloColor.copy(alpha = haloOpacity),
                    0.32F to haloColor.copy(alpha = haloOpacity * 0.96F),
                    0.62F to haloColor.copy(alpha = haloOpacity * 0.52F),
                    1F to haloColor.copy(alpha = 0F),
                    center = diamondCenter,
                    radius = 28.dp.toPx(),
                )

                onDrawBehind {
                    // Read animation state only while drawing: text and layout stay untouched.
                    val driftX = sin(phase.value)
                    val driftY = cos(phase.value)
                    drawMeshSpot(
                        brush = sky,
                        center = Offset(size.width - glowWidth * 0.18F, size.height * 0.45F),
                        radiusX = glowWidth * 0.94F,
                        radiusY = size.height * 1.35F,
                    )
                    drawMeshSpot(
                        brush = periwinkle,
                        center = Offset(
                            size.width - glowWidth * (0.48F + driftX * 0.10F),
                            size.height * (0.83F + driftY * 0.14F),
                        ),
                        radiusX = glowWidth * 0.58F,
                        radiusY = size.height * 0.86F,
                    )
                    drawMeshSpot(
                        brush = azure,
                        center = Offset(
                            size.width - glowWidth * (0.14F + driftY * 0.10F),
                            size.height * (0.10F + driftX * 0.16F),
                        ),
                        radiusX = glowWidth * 0.59F,
                        radiusY = size.height * 0.90F,
                    )
                    drawMeshSpot(
                        brush = cyan,
                        center = Offset(
                            size.width - glowWidth * (0.02F - driftX * 0.08F),
                            size.height * (0.92F - driftY * 0.16F),
                        ),
                        radiusX = glowWidth * 0.56F,
                        radiusY = size.height * 0.84F,
                    )
                    drawMeshSpot(
                        brush = pearl,
                        center = Offset(
                            size.width - glowWidth * (0.40F - driftY * 0.12F),
                            size.height * (0.30F - driftX * 0.16F),
                        ),
                        radiusX = glowWidth * 0.46F,
                        radiusY = size.height * 0.68F,
                    )
                    drawRect(brush = leftFade, blendMode = BlendMode.DstIn)
                    // Draw the diamond light after the mask so its brightness stays independent.
                    drawRect(brush = diamondHalo)
                }
            }
    )
}

private fun meshBrush(color: Color, alpha: Float): Brush = Brush.radialGradient(
    0F to color.copy(alpha = alpha),
    0.35F to color.copy(alpha = alpha * 0.72F),
    0.7F to color.copy(alpha = alpha * 0.18F),
    1F to color.copy(alpha = 0F),
    center = Offset.Zero,
    radius = 1F,
)

private fun DrawScope.drawMeshSpot(
    brush: Brush,
    center: Offset,
    radiusX: Float,
    radiusY: Float,
) {
    withTransform({
        translate(left = center.x, top = center.y)
        scale(scaleX = radiusX, scaleY = radiusY, pivot = Offset.Zero)
    }) {
        drawCircle(brush = brush, radius = 1F, center = Offset.Zero)
    }
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
