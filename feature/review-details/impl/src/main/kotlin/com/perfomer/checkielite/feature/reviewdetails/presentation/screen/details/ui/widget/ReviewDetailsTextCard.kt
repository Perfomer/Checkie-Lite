package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview
import com.perfomer.checkielite.feature.reviewdetails.R

@Composable
internal fun ReviewDetailsTextCard(
    text: String,
    header: @Composable () -> Unit,
) {
    OutlinedCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            header()

            SelectionContainer {
                Text(
                    text = text,
                    fontSize = 16.sp,
                )
            }
        }
    }
}

@Composable
internal fun ReviewDetailsTextEmptyCard(
    onClick: () -> Unit
) {
    OutlinedCard(
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ReviewDetailsTextHeader(
                title = stringResource(R.string.reviewdetails_comment),
                icon = painterResource(id = R.drawable.reviewdetails_ic_comment),
                iconTint = LocalCuiPalette.current.IconAccent,
                iconBackgroundColor = LocalCuiPalette.current.BackgroundAccentSecondary,
            )

            Text(
                text = stringResource(R.string.reviewdetails_empty_review_text),
                fontSize = 14.sp,
                color = LocalCuiPalette.current.TextSecondary,
            )

            Text(
                text = stringResource(R.string.reviewdetails_add_review_text),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LocalCuiPalette.current.TextAccent,
            )
        }
    }
}

@Composable
internal fun ReviewDetailsCommentHeader() {
    ReviewDetailsTextHeader(
        title = stringResource(R.string.reviewdetails_comment),
        icon = painterResource(id = R.drawable.reviewdetails_ic_comment),
        iconTint = LocalCuiPalette.current.IconAccent,
        iconBackgroundColor = LocalCuiPalette.current.BackgroundAccentSecondary,
    )
}

@Composable
internal fun ReviewDetailsAdvantagesHeader() {
    ReviewDetailsTextHeader(
        title = stringResource(R.string.reviewdetails_advantages),
        icon = painterResource(id = R.drawable.reviewdetails_ic_advantages),
        iconTint = LocalCuiPalette.current.IconPositive,
        iconBackgroundColor = LocalCuiPalette.current.BackgroundPositiveSecondary,
    )
}

@Composable
internal fun ReviewDetailsDisadvantagesHeader() {
    ReviewDetailsTextHeader(
        title = stringResource(R.string.reviewdetails_disadvantages),
        icon = painterResource(id = R.drawable.reviewdetails_ic_disadvantages),
        iconTint = LocalCuiPalette.current.IconNegative,
        iconBackgroundColor = LocalCuiPalette.current.BackgroundNegativeSecondary,
    )
}

@Composable
internal fun ReviewDetailsTextHeader(
    title: String,
    icon: Painter,
    iconTint: Color,
    iconBackgroundColor: Color,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Icon(
            painter = icon,
            tint = iconTint,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .background(color = iconBackgroundColor, shape = CircleShape)
                .padding(6.dp)
        )

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@WidgetPreview
@Composable
private fun ReviewDetailsTextPreview() = CheckieLiteTheme {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 24.dp)
    ) {
        ReviewDetailsTextCard(
            text = "Extraordinary. Meets an elite standard by which you judge all other restaurants. The staff is always ready to help, the premises are extremely clean, the atmosphere is lovely, and the food is both delicious and beautifully presented.",
            header = { ReviewDetailsCommentHeader() },
        )

        ReviewDetailsTextCard(
            text = "Extraordinary. Meets an elite standard by which you judge all other restaurants. The staff is always ready to help, the premises are extremely clean, the atmosphere is lovely, and the food is both delicious and beautifully presented.",
            header = { ReviewDetailsAdvantagesHeader() },
        )

        ReviewDetailsTextCard(
            text = "Extraordinary. Meets an elite standard by which you judge all other restaurants. The staff is always ready to help, the premises are extremely clean, the atmosphere is lovely, and the food is both delicious and beautifully presented.",
            header = { ReviewDetailsDisadvantagesHeader() },
        )

        ReviewDetailsTextEmptyCard(onClick = {})
    }
}