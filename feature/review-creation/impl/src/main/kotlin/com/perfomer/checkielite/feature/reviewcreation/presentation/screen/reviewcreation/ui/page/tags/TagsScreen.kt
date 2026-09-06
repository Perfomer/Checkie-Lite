package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.LocalObstruction
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.mockUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.widget.TagsLibrarySection
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.widget.TagsSearchField
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.widget.TagsStatPill
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.TagsPageUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget.ReviewCreationPageHeader
import kotlin.math.max
import kotlin.math.min
import kotlinx.coroutines.launch

private const val SEARCH_ITEM_KEY = "search"
private const val SEARCH_ITEM_INDEX = 1

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TagsScreen(
    state: TagsPageUiState,
    scrollState: LazyListState = rememberLazyListState(),
    onTagSortClick: () -> Unit = {},
    onSelectedTagsClearClick: () -> Unit = {},
    onCreateTagClick: () -> Unit = {},
    onTagClick: (id: String) -> Unit = {},
    onTagLongClick: (id: String) -> Unit = {},
    onSearchQueryInput: (text: String) -> Unit = {},
    onSearchQueryClearClick: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val palette = LocalCuiPalette.current
    val sectionBorderColor = remember(palette) { palette.OutlineSecondary.copy(alpha = 0.72F) }
    val selectedTagsCount = remember(state.tags) { state.tags.count(TagsPageUiState.Tag::isSelected) }
    val recommendedTagsCount = remember(state.tags) { state.tags.count(TagsPageUiState.Tag::isRecommended) }
    val density = LocalDensity.current
    val navigationBarsBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0
    val bottomContentPadding = 104.dp + if (isImeVisible) 0.dp else navigationBarsBottomPadding
    val shouldShowRecommendedTagsButton by remember(
        state.searchQuery,
        recommendedTagsCount,
        scrollState,
    ) {
        derivedStateOf {
            state.searchQuery.isBlank() &&
                    recommendedTagsCount > 0 &&
                    !scrollState.isItemVisible(SEARCH_ITEM_KEY)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
    ) {
        LazyColumn(
            state = scrollState,
            contentPadding = PaddingValues(
                top = LocalObstruction.current.calculateTopPadding() + 16.dp,
                bottom = bottomContentPadding,
            ),
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            item(key = "header") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    TagsHeader(
                        state = state,
                        selectedTagsCount = selectedTagsCount,
                        recommendedTagsCount = recommendedTagsCount,
                        sectionBorderColor = sectionBorderColor,
                        searchQuery = state.searchQuery,
                        onTagSortClick = onTagSortClick,
                        onSelectedTagsClearClick = onSelectedTagsClearClick,
                    )
                }
            }

            item(key = SEARCH_ITEM_KEY) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .padding(horizontal = 24.dp)
                ) {
                    TagsSearchField(
                        searchQuery = state.searchQuery,
                        onSearchQueryInput = onSearchQueryInput,
                        onSearchQueryClearClick = {
                            onSearchQueryClearClick()
                            focusManager.clearFocus()
                        },
                    )
                }
            }

            item(key = "library") {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TagsLibrarySection(
                        tags = state.tags,
                        shouldShowAddTag = state.shouldShowAddTag,
                        palette = palette,
                        sectionBorderColor = sectionBorderColor,
                        searchQuery = state.searchQuery,
                        onCreateTagClick = {
                            focusManager.clearFocus()
                            onCreateTagClick()
                        },
                        onTagClick = onTagClick,
                        onTagLongClick = onTagLongClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 8.dp),
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = shouldShowRecommendedTagsButton,
            enter = slideInVertically(initialOffsetY = { -it / 2 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it / 2 }) + fadeOut(),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                RecommendedTagsButton(
                    recommendedTagsCount = recommendedTagsCount,
                    onClick = {
                        focusManager.clearFocus()
                        coroutineScope.launch {
                            scrollState.animateScrollToItem(SEARCH_ITEM_INDEX)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                        .padding(horizontal = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun RecommendedTagsButton(
    recommendedTagsCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalCuiPalette.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = CircleShape,
            )
            .background(palette.BackgroundAccentPrimary, CircleShape)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = stringResource(R.string.reviewcreation_tags_recommended_scroll, recommendedTagsCount),
            color = palette.TextInverted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )

        Icon(
            painter = painterResource(CommonDrawable.ic_arrow_back),
            contentDescription = null,
            tint = palette.TextInverted,
            modifier = Modifier
                .size(16.dp)
                .graphicsLayer {
                    rotationZ = 90F
                }
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagsHeader(
    state: TagsPageUiState,
    selectedTagsCount: Int,
    recommendedTagsCount: Int,
    sectionBorderColor: Color,
    searchQuery: String,
    onTagSortClick: () -> Unit,
    onSelectedTagsClearClick: () -> Unit,
) {
    val palette = LocalCuiPalette.current
    val isDarkTheme = isSystemInDarkTheme()
    val hotPillBackgroundColor = remember(palette, isDarkTheme) {
        if (isDarkTheme) palette.BackgroundAccentSecondary else palette.BackgroundAccentTertiary
    }
    val hotPillBorderColor = remember(palette, isDarkTheme) {
        if (isDarkTheme) palette.OutlineAccentSecondary.copy(alpha = 0.72F)
        else palette.OutlineAccentSecondary.copy(alpha = 0.32F)
    }
    val hotPillTextColor = remember(palette, isDarkTheme) {
        if (isDarkTheme) palette.TextAccent else palette.TextAccent.copy(alpha = 0.92F)
    }
    val targetTagSortAlpha = if (searchQuery.isBlank()) 1F else 0F
    val animatedTagSortAlpha by animateFloatAsState(
        targetValue = targetTagSortAlpha,
        label = "TagsHeaderSortAlpha",
    )

    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        ReviewCreationPageHeader(
            title = stringResource(R.string.reviewcreation_tags_title),
            productPictureUri = state.mainPictureUri,
            productName = state.productName,
            endIcon = {
                CuiIconButton(
                    painter = painterResource(CommonDrawable.ic_sort),
                    onClick = onTagSortClick,
                    modifier = Modifier.graphicsLayer {
                        alpha = animatedTagSortAlpha
                        scaleX = animatedTagSortAlpha
                        scaleY = animatedTagSortAlpha
                    }
                )
            },
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TagsStatPill(
                text = stringResource(R.string.reviewcreation_tags_badge_selected, selectedTagsCount),
                backgroundColor = palette.BackgroundAccentTertiary,
                borderColor = palette.OutlineAccentSecondary,
                textColor = palette.TextAccent,
                trailingPainter = painterResource(CommonDrawable.ic_cross).takeIf { selectedTagsCount > 0 },
                onClick = onSelectedTagsClearClick.takeIf { selectedTagsCount > 0 },
            )

            TagsStatPill(
                text = stringResource(R.string.reviewcreation_tags_badge_total, state.tags.size),
                backgroundColor = palette.BackgroundPrimary.copy(alpha = 0.92F),
                borderColor = sectionBorderColor,
                textColor = palette.TextSecondary,
            )

            if (recommendedTagsCount > 0) {
                TagsStatPill(
                    text = stringResource(R.string.reviewcreation_tags_badge_recommended, recommendedTagsCount),
                    backgroundColor = hotPillBackgroundColor,
                    borderColor = hotPillBorderColor,
                    textColor = hotPillTextColor,
                )
            }
        }
    }
}

@Composable
@ScreenPreview
private fun TagsScreenPreview() = CheckieLiteTheme {
    TagsScreen(
        state = mockUiState.tagsState,
    )
}

private fun LazyListState.isItemVisible(itemKey: Any): Boolean {
    val layoutInfo = layoutInfo
    if (layoutInfo.visibleItemsInfo.isEmpty()) return true

    val itemInfo = layoutInfo.visibleItemsInfo.firstOrNull { it.key == itemKey } ?: return false
    val visibleStart = max(itemInfo.offset, layoutInfo.viewportStartOffset)
    val visibleEnd = min(itemInfo.offset + itemInfo.size, layoutInfo.viewportEndOffset)

    return visibleEnd > visibleStart
}
