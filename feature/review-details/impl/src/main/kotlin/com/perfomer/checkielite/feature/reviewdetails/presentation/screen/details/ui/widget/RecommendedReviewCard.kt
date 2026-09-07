package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.modifier.softShadow
import com.perfomer.checkielite.common.ui.cui.widget.rating.ReviewRatingVertical
import com.perfomer.checkielite.common.ui.cui.widget.text.CuiFadedText
import com.perfomer.checkielite.common.ui.presentation.transition.ReviewSharedTextElement
import com.perfomer.checkielite.common.ui.presentation.transition.SharedImage
import com.perfomer.checkielite.common.ui.presentation.transition.SharedNavigationContainer
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.common.ui.util.resource.text.text
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationContent
import com.perfomer.checkielite.core.navigation.transition.sharedNavigationElement
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.RecommendedReview

@Composable
internal fun RecommendedReviewCard(
    review: RecommendedReview,
    onClick: (reviewId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    SharedNavigationContent(id = review.reviewId) {
        SharedNavigationContainer(
            cornerRadius = 20.dp,
            overlayCornerRadius = 0.dp,
            color = LocalCuiPalette.current.BackgroundElevationBase,
            overlayColor = lerp(
                LocalCuiPalette.current.BackgroundPrimary,
                LocalCuiPalette.current.BackgroundAccentTertiary,
                0.4F,
            ),
            modifier = modifier
                .size(width = 148.dp, height = 200.dp)
                .softShadow(interactionSource = interactionSource, shape = RoundedCornerShape(20.dp))
        ) {
            RecommendedReviewCardContent(
                review = review,
                interactionSource = interactionSource,
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun RecommendedReviewCardContent(
    review: RecommendedReview,
    interactionSource: MutableInteractionSource,
    onClick: (reviewId: String) -> Unit,
) {
    Card(
        onClick = { onClick(review.reviewId) },
        interactionSource = interactionSource,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
        ),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            RecommendedReviewPicture(
                pictureUri = review.pictureUri,
                isSyncing = review.isSyncing,
            )

            ReviewRatingVertical(
                rating = review.rating,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(LocalCuiPalette.current.BackgroundElevationContent)
                    .padding(12.dp)
            ) {
                val hasBrand = review.brandName != null

                if (hasBrand) {
                    CuiFadedText(
                        text = text(review.brandName),
                        maxLines = 1,
                        fontSize = 12.sp,
                        color = LocalCuiPalette.current.TextAccent,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall.copy(
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .sharedNavigationElement(
                                element = ReviewSharedTextElement.Subtitle,
                            )
                    )
                }

                CuiFadedText(
                    text = text(review.productName),
                    maxLines = if (hasBrand) 1 else 2,
                    fontSize = 14.sp,
                    color = LocalCuiPalette.current.TextPrimary,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .sharedNavigationElement(
                            element = ReviewSharedTextElement.Title,
                        )
                )
            }
        }
    }
}

@Composable
private fun RecommendedReviewPicture(
    pictureUri: String?,
    isSyncing: Boolean,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1F)
            .background(LocalCuiPalette.current.BackgroundSecondary)
    ) {
        if (pictureUri == null) {
            Icon(
                painter = painterResource(id = CommonDrawable.ic_image),
                tint = LocalCuiPalette.current.IconTertiary,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        } else {
            SharedImage(
                imageUri = pictureUri,
                cornerRadius = 20.dp,
                overlayCornerRadius = 24.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        color = LocalCuiPalette.current.OutlinePicture,
                    )
            )
        }

        AnimatedVisibility(visible = isSyncing, enter = fadeIn(), exit = fadeOut()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(LocalCuiPalette.current.BackgroundPrimary.copy(alpha = 0.5F))
                    .padding(12.dp)
            ) {
                CircularProgressIndicator(
                    color = LocalCuiPalette.current.IconPrimary,
                    strokeWidth = 2.dp,
                )
            }
        }
    }
}

@Composable
@WidgetPreview
private fun RecommendedReviewCardPreview() = CheckieLiteTheme {
    RecommendedReviewCard(
        review = RecommendedReview(
            reviewId = "",
            brandName = Text.raw("DARKSIDE"),
            productName = Text.raw("Lemonblast"),
            pictureUri = null,
            rating = 10,
            isSyncing = false,
        ),
        onClick = {},
    )
}
