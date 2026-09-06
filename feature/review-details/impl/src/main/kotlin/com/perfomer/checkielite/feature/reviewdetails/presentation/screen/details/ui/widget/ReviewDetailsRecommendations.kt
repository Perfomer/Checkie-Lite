package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.cui.widget.cell.CuiReviewCard
import com.perfomer.checkielite.common.ui.cui.widget.cell.ReviewItem
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.util.resource.text.text
import com.perfomer.checkielite.feature.reviewdetails.R
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.RecommendedReview
import kotlinx.collections.immutable.ImmutableList

internal fun LazyListScope.reviewDetailsRecommendations(
    recommendations: ImmutableList<RecommendedReview>,
    onRecommendationClick: (recommendedReviewId: String) -> Unit,
) {
    if (recommendations.isEmpty()) return

    item(key = "recommendations") {
        Text(
            text = stringResource(R.string.reviewdetails_recommendations),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = LocalCuiPalette.current.TextPrimary,
            modifier = Modifier.padding(horizontal = 20.dp).padding(top = 28.dp, bottom = 16.dp)
        )
    }
    items(items = recommendations, key = { "recommendation:${it.reviewId}" }) { review ->
        CuiReviewCard(
            item = ReviewItem(
                id = review.reviewId,
                title = text(review.productName),
                brand = review.brandName?.let { text(it) },
                imageUri = review.pictureUri,
                rating = review.rating,
                isSyncing = review.isSyncing,
            ),
            onClick = onRecommendationClick,
            // Details-to-details navigation does not request a shared transition.
            isTransitionEnabled = { false },
            modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 12.dp)
        )
    }
}
