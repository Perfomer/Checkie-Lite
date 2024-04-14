package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gigamole.composefadingedges.FadingEdgesGravity
import com.gigamole.composefadingedges.horizontalFadingEdges
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.widget.rating.ReviewRatingVertical
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.RecommendedReview

@Composable
internal fun RecommendedReviewCard(
    review: RecommendedReview,
    onClick: (reviewId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = { onClick(review.reviewId) },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp,
            pressedElevation = 0.dp,
        ),
        modifier = modifier.size(width = 148.dp, height = 200.dp)
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
                    .background(LocalCuiPalette.current.BackgroundPrimary)
                    .padding(12.dp)
            ) {
                val hasBrand = review.brandName != null

                if (hasBrand) {
                    Text(
                        text = review.brandName!!,
                        maxLines = 1,
                        fontSize = 12.sp,
                        color = LocalCuiPalette.current.TextAccent,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall.copy(
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        ),
                        overflow = TextOverflow.Visible,
                        softWrap = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalFadingEdges(gravity = FadingEdgesGravity.End, length = 40.dp)
                    )
                }

                Text(
                    text = review.productName,
                    maxLines = if (hasBrand) 1 else 2,
                    fontSize = 14.sp,
                    color = LocalCuiPalette.current.TextPrimary,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Visible,
                    softWrap = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalFadingEdges(gravity = FadingEdgesGravity.End, length = 40.dp)
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
            AsyncImage(
                model = pictureUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        AnimatedVisibility(visible = isSyncing, enter = fadeIn(), exit = fadeOut()) {
            Box(
                modifier = Modifier
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
        review = RecommendedReview("", "DARKSIDE", "Lemonblast", null, 10, false),
        onClick = {},
    )
}