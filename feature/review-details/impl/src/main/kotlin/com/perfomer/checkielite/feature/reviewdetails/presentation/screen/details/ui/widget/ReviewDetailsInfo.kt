package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.cui.modifier.conditional
import com.perfomer.checkielite.common.ui.cui.widget.rating.ReviewReaction
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.feature.reviewdetails.R
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.Price

@Composable
internal fun ReviewDetailsInfo(
    date: String,
    rating: Int,
    price: Price?,
    onEmptyPriceClick: () -> Unit,
) {
    Spacer(Modifier.height(20.dp))

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
            .padding(horizontal = 16.dp)
    ) {
        val reviewReaction = remember(rating) { ReviewReaction.createFromRating(rating) }
        val ratingValue = remember(rating) { "$rating/10" }

        InfoCell(
            value = AnnotatedString(ratingValue),
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
                value = buildAnnotatedString {
                    append(price.value)

                    if (price.fractionalPartIndices != null) {
                        addStyle(SpanStyle(fontSize = 14.sp), price.fractionalPartIndices.first, price.fractionalPartIndices.last)
                    }
                },
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
        } else {
            InfoCell(
                value = AnnotatedString(stringResource(R.string.reviewdetails_price_specify_value)),
                valueColor = LocalCuiPalette.current.TextAccent,
                description = stringResource(R.string.reviewdetails_price_specify_description),
                descriptionColor = LocalCuiPalette.current.TextAccent,
                descriptionOffset = (-3).dp,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.reviewdetails_ic_price),
                        contentDescription = null,
                        tint = LocalCuiPalette.current.IconAccent,
                    )
                },
                iconBackgroundColor = LocalCuiPalette.current.BackgroundAccentSecondary,
                fromLeft = false,
                onClick = onEmptyPriceClick,
                modifier = Modifier.weight(1F)
            )
        }
    }
}

@Composable
private fun InfoCell(
    value: AnnotatedString,
    description: String,
    icon: @Composable BoxScope.() -> Unit,
    fromLeft: Boolean,
    modifier: Modifier = Modifier,
    valueColor: Color = Color.Unspecified,
    descriptionColor: Color = LocalCuiPalette.current.TextSecondary,
    iconBackgroundColor: Color = LocalCuiPalette.current.BackgroundSecondary,
    descriptionOffset: Dp = 0.dp,
    onClick: (() -> Unit)? = null,
) {
    @Composable
    fun IconBox() {
        Box(
            contentAlignment = Alignment.Center,
            content = icon,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(iconBackgroundColor)
        )
    }

    Row(
        horizontalArrangement = if (fromLeft) Arrangement.Start else Arrangement.End,
        modifier = modifier
    ) {
        val startPadding = if (fromLeft) 8.dp else 12.dp
        val endPadding = if (fromLeft) 12.dp else 8.dp

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (fromLeft) Arrangement.Start else Arrangement.End,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .conditional(onClick != null) { clickable(onClick = onClick!!) }
                .padding(vertical = 4.dp)
                .padding(start = startPadding, end = endPadding)
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
                    color = valueColor,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodySmall.copy(
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                )

                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = descriptionColor,
                    style = MaterialTheme.typography.bodySmall.copy(
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier.offset(y = descriptionOffset)
                )
            }

            if (!fromLeft) {
                Spacer(Modifier.width(8.dp))
                IconBox()
            }
        }
    }
}
