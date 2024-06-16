package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.feature.reviewdetails.R
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.RecommendedReview
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ReviewDetailsRecommendations(
    recommendations: ImmutableList<RecommendedReview>,
    onRecommendationClick: (recommendedReviewId: String) -> Unit,
) {
    if (recommendations.isNotEmpty()) {
        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.reviewdetails_recommendations),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(top = 20.dp, start = 24.dp, end = 24.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(
                items = recommendations,
                key = { _, item -> item.reviewId }
            ) { _, item ->
                RecommendedReviewCard(
                    review = item,
                    onClick = onRecommendationClick,
                )
            }
        }
    }
}