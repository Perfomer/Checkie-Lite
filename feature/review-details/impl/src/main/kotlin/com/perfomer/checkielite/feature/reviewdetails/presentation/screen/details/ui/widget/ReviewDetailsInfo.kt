package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.cui.modifier.softShadow
import com.perfomer.checkielite.common.ui.cui.modifier.thenIf
import com.perfomer.checkielite.common.ui.cui.widget.rating.ReviewReaction
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.common.ui.util.resource.text.text
import com.perfomer.checkielite.feature.reviewdetails.R
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.Price

@Composable
internal fun ReviewDetailsInfo(
    date: Text,
    rating: Int,
    price: Price?,
    onRatingClick: () -> Unit,
    onEmptyPriceClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 20.dp).padding(top = 24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.reviewdetails_review),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = LocalCuiPalette.current.TextPrimary,
            )
            Text(
                text = text(date),
                color = LocalCuiPalette.current.TextSecondary,
                fontSize = 13.sp,
            )
        }

        // Keep long prices and enlarged text readable on compact screens.
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            if (maxWidth < 320.dp || LocalDensity.current.fontScale > 1.3F) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    RatingCard(rating = rating, onClick = onRatingClick)
                    PriceCard(price = price, onEmptyPriceClick = onEmptyPriceClick)
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RatingCard(rating = rating, onClick = onRatingClick, modifier = Modifier.weight(1F))
                    PriceCard(price = price, onEmptyPriceClick = onEmptyPriceClick, modifier = Modifier.weight(1F))
                }
            }
        }
    }
}

@Composable
private fun RatingCard(
    rating: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val reaction = ReviewReaction.createFromRating(rating)
    InfoCard(
        value = buildAnnotatedString {
            append(rating.toString())
            pushStyle(SpanStyle(fontSize = 14.sp, color = LocalCuiPalette.current.TextSecondary))
            append(" / 10")
            pop()
        },
        description = stringResource(R.string.reviewdetails_rating),
        icon = {
            Image(
                painter = painterResource(reaction.drawable),
                contentDescription = stringResource(reaction.contentDescription),
                modifier = Modifier.size(32.dp)
            )
        },
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
private fun PriceCard(
    price: Price?,
    onEmptyPriceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    InfoCard(
        value = if (price == null) {
            AnnotatedString(stringResource(R.string.reviewdetails_price_specify_value))
        } else {
            buildAnnotatedString {
                append(text(price.value))
                price.fractionalPartIndices?.let { indices ->
                    addStyle(SpanStyle(fontSize = 14.sp), indices.first, indices.last)
                }
            }
        },
        description = stringResource(R.string.reviewdetails_price),
        valueColor = if (price == null) LocalCuiPalette.current.TextAccent else LocalCuiPalette.current.TextPrimary,
        icon = {
            Icon(
                painter = painterResource(R.drawable.reviewdetails_ic_price),
                contentDescription = null,
                tint = LocalCuiPalette.current.IconAccent,
                modifier = Modifier
                    .size(32.dp)
                    .background(LocalCuiPalette.current.BackgroundAccentSecondary, CircleShape)
                    .padding(6.dp)
            )
        },
        onClick = if (price == null) onEmptyPriceClick else null,
        modifier = modifier
    )
}

@Composable
private fun InfoCard(
    value: AnnotatedString,
    description: String,
    icon: @Composable () -> Unit,
    valueColor: Color = LocalCuiPalette.current.TextPrimary,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)
    Surface(
        shape = shape,
        color = LocalCuiPalette.current.BackgroundElevationBase,
        modifier = modifier.fillMaxWidth().softShadow(shape = shape)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .thenIf(onClick != null) { clickable(onClick = { onClick?.invoke() }) }
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                icon()
                Text(
                    text = description,
                    color = LocalCuiPalette.current.TextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1F)
                )
            }
            Text(
                text = value,
                color = valueColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
            )
        }
    }
}
