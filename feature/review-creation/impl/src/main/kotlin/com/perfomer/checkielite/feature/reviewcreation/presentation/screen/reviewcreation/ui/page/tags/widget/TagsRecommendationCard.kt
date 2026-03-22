package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.widget

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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.theme.CuiColorToken
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.TagsPageUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TagsRecommendationCard(
    modifier: Modifier = Modifier,
    recommendedTags: List<TagsPageUiState.Tag>,
    palette: CuiPalette,
    onTagClick: (String) -> Unit,
    onTagLongClick: (String) -> Unit,
) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(32.dp)
    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF2684D),
            Color(0xFFF58E54),
            Color(0xFFF8C96D),
        ),
        start = Offset.Zero,
        end = Offset(1080F, 520F),
    )
    val border = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.45F),
            Color.White.copy(alpha = 0.12F),
        ),
        start = Offset.Zero,
        end = Offset(640F, 0F),
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(gradient)
            .border(width = 1.dp, brush = border, shape = shape)
            .drawWithCache {
                val glowRadius = 66.dp.toPx()
                val glowCenter = Offset(
                    x = size.width + 26.dp.toPx() - glowRadius,
                    y = -42.dp.toPx() + glowRadius,
                )

                onDrawWithContent {
                    drawContent()
                    drawCircle(
                        color = Color.White.copy(alpha = 0.10F),
                        radius = glowRadius,
                        center = glowCenter,
                    )
                }
            }
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1F)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(Color.White.copy(alpha = 0.18F))
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_magic),
                            contentDescription = null,
                            tint = CuiColorToken.White1,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.reviewcreation_tags_recommended_title),
                            fontSize = 18.sp,
                            color = CuiColorToken.White1,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Text(
                            text = stringResource(R.string.reviewcreation_tags_recommended_subtitle),
                            fontSize = 12.sp,
                            color = CuiColorToken.White1.copy(alpha = 0.86F),
                            lineHeight = 16.sp,
                        )
                    }
                }

                TagsStatPill(
                    text = stringResource(R.string.reviewcreation_tags_badge_recommended, recommendedTags.size),
                    backgroundColor = Color.White.copy(alpha = 0.20F),
                    borderColor = Color.White.copy(alpha = 0.24F),
                    textColor = CuiColorToken.White1,
                )
            }

            CuiSpacer(14.dp)

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                for (tag in recommendedTags) {
                    TagsTagChip(
                        tag = tag,
                        palette = palette,
                        sectionBorderColor = Color.White.copy(alpha = 0.28F),
                        isRecommended = true,
                        onClick = onTagClick,
                        onLongClick = onTagLongClick,
                    )
                }
            }
        }
    }
}
