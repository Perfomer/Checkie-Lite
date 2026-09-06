package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.modifier.softShadow
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChip
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChipStyle
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiTagChip
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.util.resource.text.text
import com.perfomer.checkielite.feature.reviewdetails.R
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.Tag
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ReviewDetailsTags(
    tags: ImmutableList<Tag>,
    onAddTagsClick: () -> Unit,
    onTagClick: (tagId: String) -> Unit,
) {
    val palette = LocalCuiPalette.current
    val chipStyle = CuiChipStyle.default().copy(
        iconBackgroundColor = palette.BackgroundElevationBase,
        textBackgroundColor = palette.BackgroundElevationBase,
        borderColor = Color.Transparent,
        borderWidth = 0.dp,
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(top = 28.dp)
    ) {
        Text(
            text = stringResource(R.string.reviewdetails_tags),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = palette.TextPrimary,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            for (tag in tags) {
                key(tag.tagId) {
                    val interactionSource = remember { MutableInteractionSource() }
                    CuiTagChip(
                        text = text(tag.text),
                        emoji = tag.emoji,
                        onClick = { onTagClick(tag.tagId) },
                        style = chipStyle,
                        interactionSource = interactionSource,
                        modifier = Modifier.softShadow(
                            interactionSource = interactionSource,
                            shape = CircleShape,
                            radius = 8.dp,
                            offset = DpOffset(x = 0.dp, y = 2.dp),
                        )
                    )
                }
            }
            CuiChip(
                leadingIcon = {
                    Icon(
                        painter = painterResource(CommonDrawable.ic_plus),
                        contentDescription = null,
                        tint = palette.IconAccent,
                        modifier = Modifier.size(16.dp)
                    )
                },
                style = chipStyle.copy(
                    iconBackgroundColor = palette.BackgroundAccentSecondary,
                    textBackgroundColor = palette.BackgroundAccentSecondary,
                    fontWeight = FontWeight.Medium,
                ),
                onClick = onAddTagsClick,
            ) {
                Text(text = stringResource(R.string.reviewdetails_tags_add), color = palette.TextAccent)
            }
        }
    }
}
