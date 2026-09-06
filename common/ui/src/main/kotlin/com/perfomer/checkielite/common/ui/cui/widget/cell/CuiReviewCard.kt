package com.perfomer.checkielite.common.ui.cui.widget.cell

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.modifier.SharedContentKey
import com.perfomer.checkielite.common.ui.cui.modifier.SharedContentPart
import com.perfomer.checkielite.common.ui.cui.modifier.sharedNavigationContent
import com.perfomer.checkielite.common.ui.cui.modifier.softShadow
import com.perfomer.checkielite.common.ui.cui.widget.rating.ReviewRatingGlow
import com.perfomer.checkielite.common.ui.cui.widget.rating.ReviewRating
import com.perfomer.checkielite.common.ui.presentation.transition.SharedImage
import com.perfomer.checkielite.common.ui.presentation.transition.SharedNavigationContainer
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview

private val ReviewCardShape = RoundedCornerShape(24.dp)

@Composable
fun CuiReviewCard(
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
        otherColor = lerp(
            LocalCuiPalette.current.BackgroundPrimary,
            LocalCuiPalette.current.BackgroundAccentTertiary,
            0.4F,
        ),
        isEnabled = isTransitionEnabled,
        modifier = modifier.softShadow(interactionSource = interactionSource, shape = ReviewCardShape)
    ) {
        Surface(shape = ReviewCardShape, color = Color.Transparent) {
            Box {
                if (item.rating == 10) {
                    ReviewRatingGlow(modifier = Modifier.matchParentSize())
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
