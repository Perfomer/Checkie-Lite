package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.cui.widget.rating.ReviewReaction
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.feature.reviewdetails.R

@Composable
internal fun ReviewDetailsInfo(
    date: String,
    rating: Int,
    price: String?,
    onEmptyPriceClick: () -> Unit,
) {
    Spacer(Modifier.height(24.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.reviewdetails_review),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1F)
        )

        Text(
            text = date,
            color = LocalCuiPalette.current.TextSecondary,
            fontSize = 14.sp,
        )
    }

    Spacer(Modifier.height(16.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        val reviewReaction = remember(rating) { ReviewReaction.createFromRating(rating) }
        val ratingValue = remember(rating) { "$rating/10" }

        InfoCell(
            value = ratingValue,
            description = stringResource(R.string.reviewdetails_rating),
            icon = {
                Image(
                    painter = painterResource(reviewReaction.drawable),
                    contentDescription = stringResource(reviewReaction.contentDescription),
                    modifier = Modifier.size(26.dp)
                )
            },
            fromLeft = true,
            modifier = Modifier.weight(1F)
        )

        if (price != null) {
            InfoCell(
                value = price,
                description = stringResource(R.string.reviewdetails_price),
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.reviewdetails_ic_price),
                        contentDescription = null,
                        tint = LocalCuiPalette.current.IconSecondary,
                    )
                },
                fromLeft = false,
                modifier = Modifier.weight(1F)
            )
        }
    }
}

@Composable
private fun InfoCell(
    value: String,
    description: String,
    icon: @Composable BoxScope.() -> Unit,
    fromLeft: Boolean,
    modifier: Modifier = Modifier,
) {
    @Composable
    fun IconBox() {
        Box(
            contentAlignment = Alignment.Center,
            content = icon,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(LocalCuiPalette.current.BackgroundSecondary)
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (fromLeft) Arrangement.Start else Arrangement.End,
        modifier = modifier
    ) {
        if (fromLeft) {
            IconBox()
            Spacer(Modifier.width(8.dp))
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = if (fromLeft) Alignment.Start else Alignment.End,
        ) {
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.bodySmall.copy(
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
            )

            Text(
                text = description,
                fontSize = 12.sp,
                color = LocalCuiPalette.current.TextSecondary,
                style = MaterialTheme.typography.bodySmall.copy(
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
            )
        }

        if (!fromLeft) {
            Spacer(Modifier.width(8.dp))
            IconBox()
        }
    }
}
