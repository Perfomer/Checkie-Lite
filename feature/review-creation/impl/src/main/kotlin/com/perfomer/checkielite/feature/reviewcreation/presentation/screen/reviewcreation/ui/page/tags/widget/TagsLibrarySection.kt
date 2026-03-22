package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.widget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.TagsPageUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TagsLibrarySection(
    tags: List<TagsPageUiState.Tag>,
    searchQuery: String,
    shouldShowAddTag: Boolean,
    palette: CuiPalette,
    sectionShape: Shape,
    sectionBorderColor: Color,
    onCreateTagClick: () -> Unit,
    onTagSortClick: () -> Unit,
    onTagClick: (String) -> Unit,
    onTagLongClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(sectionShape)
            .background(palette.BackgroundPrimary.copy(alpha = 0.86F))
            .border(
                width = 1.dp,
                color = sectionBorderColor,
                shape = sectionShape,
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .weight(1F)
                        .padding(start = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.reviewcreation_tags_library_title),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = palette.TextPrimary,
                    )
                    Text(
                        text = stringResource(R.string.reviewcreation_tags_library_subtitle),
                        fontSize = 12.sp,
                        color = palette.TextSecondary,
                        lineHeight = 16.sp,
                    )
                }

                val targetTagSortAlpha = if (searchQuery.isBlank()) 1F else 0F
                val animatedTagSortAlpha by animateFloatAsState(
                    targetValue = targetTagSortAlpha,
                    label = "TagsLibrarySortAlpha",
                )

                CuiIconButton(
                    painter = painterResource(CommonDrawable.ic_sort),
                    onClick = onTagSortClick,
                    modifier = Modifier.graphicsLayer {
                        alpha = animatedTagSortAlpha
                        scaleX = animatedTagSortAlpha
                        scaleY = animatedTagSortAlpha
                    }
                )
            }

            CuiSpacer(16.dp)

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (shouldShowAddTag) {
                    TagsAddTagChip(
                        searchQuery = searchQuery.takeIf { it.isNotBlank() },
                        onClick = onCreateTagClick,
                    )
                }

                for (tag in tags) {
                    TagsTagChip(
                        tag = tag,
                        palette = palette,
                        sectionBorderColor = sectionBorderColor,
                        onClick = onTagClick,
                        onLongClick = onTagLongClick,
                    )
                }
            }
        }
    }
}
