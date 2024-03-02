package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun ColumnScope.ReviewDetailsText(
    reviewText: String?,
    onEmptyReviewTextClick: () -> Unit
) {
    if (reviewText != null) {
        Text(
            text = reviewText,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    } else {
        EmptyText(
            onEmptyReviewTextClick = onEmptyReviewTextClick,
        )
    }
}