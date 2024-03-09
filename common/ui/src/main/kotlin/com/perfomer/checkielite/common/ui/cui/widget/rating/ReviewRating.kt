package com.perfomer.checkielite.common.ui.cui.widget.rating

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview

@Composable
fun ReviewRating(
    rating: Int,
    modifier: Modifier = Modifier,
) {
    val reviewReaction = remember(rating) { ReviewReaction.createFromRating(rating) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = rating.toString(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )

        Text(
            text = "/10",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = LocalCuiPalette.current.TextSecondary,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Image(
            painter = painterResource(reviewReaction.drawable),
            contentDescription = stringResource(reviewReaction.contentDescription),
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
@WidgetPreview
private fun CheckieRatingPreview() = CheckieLiteTheme {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ReviewRating(rating = 8)
    }
}