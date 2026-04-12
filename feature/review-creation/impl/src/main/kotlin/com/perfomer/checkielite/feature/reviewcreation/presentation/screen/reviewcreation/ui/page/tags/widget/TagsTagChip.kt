package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChip
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChipStyle
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.theme.CuiColorToken
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.TagsPageUiState

@Composable
internal fun TagsTagChip(
    tag: TagsPageUiState.Tag,
    palette: CuiPalette,
    sectionBorderColor: Color,
    isRecommended: Boolean = false,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current

    val chipStyle = when {
        tag.isSelected -> CuiChipStyle(
            iconBackgroundColor = palette.BackgroundAccentSecondary,
            textBackgroundColor = palette.BackgroundAccentTertiary,
            borderColor = palette.OutlineAccentPrimary,
            borderWidth = 1.5.dp,
            fontWeight = FontWeight.Medium,
        )
        isRecommended -> CuiChipStyle(
            iconBackgroundColor = Color.White.copy(alpha = 0.92F),
            textBackgroundColor = Color.White.copy(alpha = 0.94F),
            borderColor = Color.White.copy(alpha = 0.28F),
            borderWidth = 1.dp,
            fontWeight = FontWeight.Medium,
        )
        else -> CuiChipStyle(
            iconBackgroundColor = palette.BackgroundPrimary,
            textBackgroundColor = palette.BackgroundPrimary.copy(alpha = 0.96F),
            borderColor = sectionBorderColor,
            borderWidth = 1.dp,
            fontWeight = FontWeight.Normal,
        )
    }

    val textColor = when {
        isRecommended && tag.isSelected -> CuiColorToken.White1
        isRecommended -> CuiColorToken.Black1
        tag.isSelected -> palette.TextAccent
        else -> palette.TextPrimary
    }

    CuiChip(
        onClick = { onClick(tag.id) },
        onLongClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            onLongClick(tag.id)
        },
        style = chipStyle,
        modifier = modifier,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (tag.emoji != null) {
                Text(text = tag.emoji, color = textColor)
                CuiSpacer(4.dp)
            }

            Box {
                Text(
                    text = tag.value,
                    color = textColor,
                )

                // Crutch to keep size reserved for medium font weight
                // to avoid text flickering after chip become selected
                Text(
                    text = tag.value,
                    color = Color.Transparent,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}
