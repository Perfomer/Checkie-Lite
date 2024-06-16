package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.cui.modifier.conditional
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

@Composable
internal fun CurrencySymbol(
    currencySymbol: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val targetBackgroundColor =
        if (isSelected) LocalCuiPalette.current.BackgroundAccentSecondary
        else LocalCuiPalette.current.BackgroundSecondary

    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(targetBackgroundColor)
                .conditional(onClick != null) { clickable(onClick = onClick!!) }
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = currencySymbol,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.bodySmall.copy(
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
            )
        }
    }
}