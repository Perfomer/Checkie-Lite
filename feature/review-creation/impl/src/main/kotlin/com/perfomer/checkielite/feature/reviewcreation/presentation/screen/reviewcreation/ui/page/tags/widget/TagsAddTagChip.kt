package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.widget

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChip
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChipStyle
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.feature.reviewcreation.R

@Composable
internal fun TagsAddTagChip(
    searchQuery: String?,
    onClick: () -> Unit,
) {
    val palette = LocalCuiPalette.current
    val style = remember(palette) {
        CuiChipStyle(
            iconBackgroundColor = palette.BackgroundAccentSecondary,
            textBackgroundColor = palette.BackgroundAccentTertiary,
            borderColor = palette.OutlineAccentPrimary,
            borderWidth = 1.dp,
            fontWeight = FontWeight.Medium,
        )
    }
    val text = if (searchQuery == null) {
        stringResource(R.string.reviewcreation_tags_create_tag_generic)
    } else {
        stringResource(R.string.reviewcreation_tags_create_tag_specific, searchQuery)
    }

    CuiChip(
        leadingIcon = {
            Icon(
                painter = painterResource(CommonDrawable.ic_plus),
                contentDescription = null,
                tint = palette.TextAccent,
                modifier = Modifier.size(16.dp),
            )
        },
        style = style,
        onClick = onClick,
    ) {
        Text(text = text, color = palette.TextAccent)
    }
}
