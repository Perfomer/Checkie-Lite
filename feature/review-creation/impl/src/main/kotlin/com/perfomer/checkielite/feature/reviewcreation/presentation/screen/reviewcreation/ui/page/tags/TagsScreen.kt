package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChip
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChipStyle
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedFieldDefaults
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.CuiColorToken
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.mockUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.TagsPageUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget.ReviewCreationPageHeader

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TagsScreen(
    state: TagsPageUiState,
    scrollState: ScrollState = rememberScrollState(),
    onTagSortClick: () -> Unit = {},
    onCreateTagClick: () -> Unit = {},
    onTagClick: (id: String) -> Unit = {},
    onTagLongClick: (id: String) -> Unit = {},
    onSearchQueryInput: (text: String) -> Unit = {},
    onSearchQueryClearClick: () -> Unit = {},
) {
    val hapticFeedback = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current
    val palette = LocalCuiPalette.current
    val recommendationCardShape = remember { RoundedCornerShape(32.dp) }
    val sectionShape = remember { RoundedCornerShape(30.dp) }
    val recommendationGradient = remember {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFF2684D),
                Color(0xFFF58E54),
                Color(0xFFF8C96D),
            ),
            start = Offset.Zero,
            end = Offset(1080F, 520F),
        )
    }
    val recommendationBorder = remember {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.45F),
                Color.White.copy(alpha = 0.12F),
            ),
            start = Offset.Zero,
            end = Offset(640F, 0F),
        )
    }
    val sectionBorderColor = remember(palette) { palette.OutlineSecondary.copy(alpha = 0.72F) }
    val selectedTagsCount = remember(state.tags) { state.tags.count(TagsPageUiState.Tag::isSelected) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-72).dp, y = 52.dp)
                .size(220.dp)
                .clip(CircleShape)
                .background(Color(0xFFF7B896).copy(alpha = 0.16F))
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 78.dp, y = 210.dp)
                .size(180.dp)
                .clip(CircleShape)
                .background(Color(0xFFFCE2AE).copy(alpha = 0.28F))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 104.dp, top = 16.dp)
        ) {
            ReviewCreationPageHeader(
                title = stringResource(R.string.reviewcreation_tags_title),
                productPictureUri = state.mainPictureUri,
                productName = state.productName,
                endIcon = {
                    val targetTagSortAlpha = if (state.searchQuery.isBlank()) 1F else 0F
                    val animatedTagSortAlpha by animateFloatAsState(targetValue = targetTagSortAlpha, label = "TagsSortAlpha")

                    CuiIconButton(
                        painter = painterResource(CommonDrawable.ic_sort),
                        onClick = onTagSortClick,
                        modifier = Modifier.graphicsLayer { alpha = animatedTagSortAlpha }
                    )
                }
            )

            CuiSpacer(24.dp)

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                StatPill(
                    text = stringResource(R.string.reviewcreation_tags_badge_selected, selectedTagsCount),
                    backgroundColor = palette.BackgroundAccentTertiary,
                    borderColor = palette.OutlineAccentSecondary,
                    textColor = palette.TextAccent,
                )

                StatPill(
                    text = stringResource(R.string.reviewcreation_tags_badge_total, state.tags.size),
                    backgroundColor = palette.BackgroundPrimary.copy(alpha = 0.92F),
                    borderColor = sectionBorderColor,
                    textColor = palette.TextSecondary,
                )

                if (state.recommendedTags.isNotEmpty()) {
                    StatPill(
                        text = stringResource(R.string.reviewcreation_tags_badge_recommended, state.recommendedTags.size),
                        backgroundColor = Color(0xFFFFE3D2),
                        borderColor = Color.White.copy(alpha = 0.6F),
                        textColor = Color(0xFFD55A2B),
                    )
                }
            }

            CuiSpacer(18.dp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(sectionShape)
                    .background(palette.BackgroundPrimary.copy(alpha = 0.88F))
                    .border(
                        width = 1.dp,
                        color = sectionBorderColor,
                        shape = sectionShape,
                    )
                    .padding(horizontal = 14.dp)
                    .padding(top = 14.dp)
            ) {
                SearchField(
                    searchQuery = state.searchQuery,
                    onSearchQueryInput = onSearchQueryInput,
                    onSearchQueryClearClick = {
                        onSearchQueryClearClick()
                        focusManager.clearFocus()
                    },
                )
            }

            @Composable
            fun TagChip(tag: TagsPageUiState.Tag, isRecommended: Boolean = false) {
                val chipStyle = when {
                    isRecommended && tag.isSelected -> CuiChipStyle(
                        iconBackgroundColor = Color.White.copy(alpha = 0.28F),
                        textBackgroundColor = Color.White.copy(alpha = 0.22F),
                        borderColor = Color.White.copy(alpha = 0.62F),
                        borderWidth = 1.dp,
                        fontWeight = FontWeight.Medium,
                    )

                    isRecommended -> CuiChipStyle(
                        iconBackgroundColor = Color.White.copy(alpha = 0.92F),
                        textBackgroundColor = Color.White.copy(alpha = 0.94F),
                        borderColor = Color.White.copy(alpha = 0.28F),
                        borderWidth = 1.dp,
                        fontWeight = FontWeight.Medium,
                    )

                    tag.isSelected -> CuiChipStyle(
                        iconBackgroundColor = palette.BackgroundAccentSecondary,
                        textBackgroundColor = palette.BackgroundAccentTertiary,
                        borderColor = palette.OutlineAccentPrimary,
                        borderWidth = 1.5.dp,
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
                    onClick = { onTagClick(tag.id) },
                    onLongClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onTagLongClick(tag.id)
                    },
                    style = chipStyle,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (tag.emoji != null) {
                            Text(text = tag.emoji, color = textColor)
                            CuiSpacer(4.dp)
                        }

                        Text(
                            text = tag.value,
                            color = textColor,
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = state.recommendedTags.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Column {
                    CuiSpacer(18.dp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(recommendationCardShape)
                            .background(recommendationGradient)
                            .border(
                                width = 1.dp,
                                brush = recommendationBorder,
                                shape = recommendationCardShape,
                            )
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
                            .animateContentSize()
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
                                            .clip(CircleShape)
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
                                        Modifier.offset(y = 4.dp)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.reviewcreation_tags_recommended_title),
                                            fontSize = 18.sp,
                                            color = CuiColorToken.White1,
                                            fontWeight = FontWeight.SemiBold,
                                        )

                                        Text(
                                            text = stringResource(R.string.reviewcreation_tags_recommended_subtitle),
                                            fontSize = 12.sp,
                                            color = CuiColorToken.White1.copy(alpha = 0.86F),
                                            modifier = Modifier.offset(y = (-4).dp)
                                        )
                                    }
                                }

                                StatPill(
                                    text = stringResource(R.string.reviewcreation_tags_badge_recommended, state.recommendedTags.size),
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
                                for (tag in state.recommendedTags) {
                                    TagChip(tag, isRecommended = true)
                                }
                            }
                        }
                    }
                }
            }

            CuiSpacer(16.dp)

            Box(
                modifier = Modifier
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
                            modifier = Modifier
                                .weight(1F)
                                .padding(start = 4.dp)
                                .offset(y = 1.dp)
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
                                modifier = Modifier.offset(y = (-2).dp)
                            )
                        }

                        StatPill(
                            text = stringResource(R.string.reviewcreation_tags_badge_total, state.tags.size),
                            backgroundColor = palette.BackgroundSecondary,
                            borderColor = sectionBorderColor,
                            textColor = palette.TextSecondary,
                        )
                    }

                    CuiSpacer(16.dp)

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        if (state.shouldShowAddTag) {
                            AddTagChip(
                                searchQuery = state.searchQuery.takeIf { it.isNotBlank() },
                                onCreateTagClick = {
                                    focusManager.clearFocus()
                                    onCreateTagClick()
                                },
                            )
                        }

                        for (tag in state.tags) {
                            TagChip(tag)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddTagChip(
    searchQuery: String?,
    onCreateTagClick: () -> Unit
) {
    val palette = LocalCuiPalette.current

    val addTagStyle = remember {
        CuiChipStyle(
            iconBackgroundColor = palette.BackgroundAccentSecondary,
            textBackgroundColor = palette.BackgroundAccentTertiary,
            borderColor = palette.OutlineAccentPrimary,
            borderWidth = 1.dp,
            fontWeight = FontWeight.Medium,
        )
    }

    val addTagText = if (searchQuery == null) {
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
        style = addTagStyle,
        onClick = onCreateTagClick,
    ) {
        Text(addTagText, color = palette.TextAccent)
    }
}

@Composable
private fun StatPill(
    text: String,
    backgroundColor: Color,
    borderColor: Color,
    textColor: Color,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .border(width = 1.dp, color = borderColor, shape = CircleShape)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
        )
    }
}

@Composable
private fun SearchField(
    searchQuery: String,
    onSearchQueryInput: (text: String) -> Unit,
    onSearchQueryClearClick: () -> Unit,
) {
    CuiOutlinedField(
        text = searchQuery,
        placeholder = stringResource(R.string.reviewcreation_tags_field_search),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            capitalization = KeyboardCapitalization.Sentences,
        ),
        singleLine = true,
        onValueChange = onSearchQueryInput,
        colors = CuiOutlinedFieldDefaults.colors(unfocusedBorderColor = LocalCuiPalette.current.OutlineSecondary),
        trailingIcon = {
            if (searchQuery.isBlank()) {
                Icon(
                    painter = painterResource(id = CommonDrawable.ic_search),
                    tint = LocalCuiPalette.current.IconSecondary,
                    contentDescription = null,
                    modifier = Modifier.offset(x = (-4).dp)
                )
            } else {
                CuiIconButton(
                    painter = painterResource(id = CommonDrawable.ic_cross),
                    contentDescription = stringResource(CommonString.common_clear),
                    tint = LocalCuiPalette.current.IconPrimary,
                    onClick = onSearchQueryClearClick,
                    modifier = Modifier.offset(x = (-4).dp)
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
@ScreenPreview
private fun TagsScreenPreview() = CheckieLiteTheme {
    TagsScreen(
        state = mockUiState.tagsState,
    )
}
