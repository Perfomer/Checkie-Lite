package com.perfomer.checkielite.common.ui.cui.widget.chip

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview

@Stable
data class CuiChipStyle(
    val iconBackgroundColor: Color,
    val textBackgroundColor: Color,
    val borderColor: Color,
    val borderWidth: Dp,
    val fontWeight: FontWeight,
) {
    companion object {

        @Composable
        @ReadOnlyComposable
        fun default(palette: CuiPalette = LocalCuiPalette.current): CuiChipStyle {
            return CuiChipStyle(
                iconBackgroundColor = palette.BackgroundSecondary,
                textBackgroundColor = palette.BackgroundSecondary,
                borderColor = palette.OutlineSecondary,
                borderWidth = 1.dp,
                fontWeight = FontWeight.Normal,
            )
        }

        @Composable
        @ReadOnlyComposable
        fun selected(palette: CuiPalette = LocalCuiPalette.current): CuiChipStyle {
            return CuiChipStyle(
                iconBackgroundColor = palette.BackgroundAccentSecondary,
                textBackgroundColor = palette.BackgroundAccentSecondary,
                borderColor = palette.OutlineAccentPrimary,
                borderWidth = 1.5.dp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CuiChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    leadingIcon: (@Composable BoxScope.() -> Unit)? = null,
    style: CuiChipStyle = CuiChipStyle.default(),
    content: @Composable BoxScope.() -> Unit,
) {
    val localTextStyle = LocalTextStyle.current
    val textStyle = remember(style) {
        localTextStyle.copy(fontWeight = style.fontWeight, fontSize = 14.sp)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(CircleShape)
            .background(style.textBackgroundColor)
            .border(width = style.borderWidth, color = style.borderColor, shape = CircleShape)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            )
    ) {
        if (leadingIcon != null) {
            Box(
                content = leadingIcon,
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .height(28.dp)
                    .background(style.iconBackgroundColor)
                    .padding(horizontal = 8.dp)
            )
        } else {
            Spacer(Modifier.width(4.dp))
        }

        Box(
            modifier = Modifier
                .padding(start = 8.dp, end = 12.dp)
                .padding(vertical = 2.dp)
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides textStyle,
                content = { content() },
            )
        }
    }
}

@Composable
@WidgetPreview
private fun CuiChipPreview() {
    CheckieLiteTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            CuiChip(
                onClick = {},
                leadingIcon = { Text("\uD83C\uDF57") }, style = CuiChipStyle.default()
            ) {
                Text("Food")
            }

            CuiChip(
                onClick = {},
                leadingIcon = { Text("\uD83D\uDC14") }, style = CuiChipStyle.selected()
            ) {
                Text("Chicken")
            }

            CuiChip(
                onClick = {},
                style = CuiChipStyle.default()
            ) {
                Text("Household")
            }

            CuiChip(
                onClick = {},
                style = CuiChipStyle.selected()
            ) {
                Text("Quests")
            }
        }
    }
}