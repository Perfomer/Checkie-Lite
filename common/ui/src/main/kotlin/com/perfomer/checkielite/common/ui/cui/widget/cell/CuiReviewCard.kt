package com.perfomer.checkielite.common.ui.cui.widget.cell

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.modifier.softShadow
import com.perfomer.checkielite.common.ui.cui.widget.rating.ReviewRatingGlow
import com.perfomer.checkielite.common.ui.presentation.transition.SharedNavigationContainer
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview

private val ReviewCardShape = RoundedCornerShape(24.dp)

@Composable
fun CuiReviewCard(
    item: ReviewItem,
    onClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    SharedNavigationContainer(
        cornerRadius = 24.dp,
        overlayCornerRadius = 0.dp,
        color = LocalCuiPalette.current.BackgroundElevationBase,
        overlayColor = lerp(
            LocalCuiPalette.current.BackgroundPrimary,
            LocalCuiPalette.current.BackgroundAccentTertiary,
            0.4F,
        ),
        modifier = modifier.softShadow(interactionSource = interactionSource, shape = ReviewCardShape)
    ) { isTransitionActive ->
        Surface(shape = ReviewCardShape, color = Color.Transparent) {
            Box {
                if (item.rating == 10) {
                    val glowOpacity = animateFloatAsState(
                        targetValue = if (isTransitionActive) 0F else 1F,
                        animationSpec = if (isTransitionActive) snap() else tween(600),
                        label = "Review rating glow opacity",
                    )
                    ReviewRatingGlow(
                        modifier = Modifier
                            .matchParentSize()
                            .graphicsLayer {
                                alpha = if (isTransitionActive) 0F else glowOpacity.value
                            }
                    )
                }

                CuiReviewHorizontalItem(
                    item = item,
                    onClick = onClick,
                    interactionSource = interactionSource,
                    contentPadding = PaddingValues(start = 8.dp, top = 8.dp, end = 12.dp, bottom = 8.dp),
                    imageCornerRadius = 16.dp,
                    imageSize = 56.dp,
                    imageRightOffset = 12.dp,
                )
            }
        }
    }
}

@ScreenPreview
@Composable
private fun CuiReviewCardLightPreview() = CuiReviewCardPreview(darkTheme = false)

@ScreenPreview
@Composable
private fun CuiReviewCardDarkPreview() = CuiReviewCardPreview(darkTheme = true)

@Composable
private fun CuiReviewCardPreview(darkTheme: Boolean) = CheckieLiteTheme(darkTheme = darkTheme) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .background(LocalCuiPalette.current.BackgroundAccentTertiary)
            .padding(20.dp)
    ) {
        listOf(10, 9, 10, 0).forEachIndexed { index, rating ->
            CuiReviewCard(
                item = ReviewItem(
                    id = index.toString(),
                    title = "Strawberry Kiwi � a very long product name",
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
