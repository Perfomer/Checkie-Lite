package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.mockUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.widget.TagsLibrarySection
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.widget.TagsRecommendationCard
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.widget.TagsSearchField
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.widget.TagsStatPill
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.TagsPageUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget.ReviewCreationPageHeader

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
    val palette = LocalCuiPalette.current
    val sectionShape = remember { RoundedCornerShape(30.dp) }
    val sectionBorderColor = remember(palette) { palette.OutlineSecondary.copy(alpha = 0.72F) }
    val selectedTagsCount = remember(state.tags) { state.tags.count(TagsPageUiState.Tag::isSelected) }
    val density = LocalDensity.current
    val navigationBarsBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0
    val bottomContentPadding = 104.dp + if (isImeVisible) 0.dp else navigationBarsBottomPadding

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundDecoration()

        LazyColumn(
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 16.dp,
                bottom = bottomContentPadding,
            ),
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            item(key = "header") {
                TagsHeader(
                    state = state,
                    selectedTagsCount = selectedTagsCount,
                    sectionBorderColor = sectionBorderColor,
                    onSelectedTagsClearClick = onSelectedTagsClearClick,
                )
            }

            if (state.recommendedTags.isNotEmpty()) {
                item(key = "recommendations") {
                    TagsRecommendationCard(
                        recommendedTags = state.recommendedTags,
                        palette = palette,
                        onTagClick = onTagClick,
                        onTagLongClick = onTagLongClick,
                        modifier = Modifier.animateItem()
                    )
                }
            }

            item(key = "search") {
                SearchSection(
                    searchQuery = state.searchQuery,
                    sectionShape = sectionShape,
                    sectionBorderColor = sectionBorderColor,
                    onSearchQueryInput = onSearchQueryInput,
                    onSearchQueryClearClick = {
                        onSearchQueryClearClick()
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.animateItem()
                )
            }

            item(key = "library") {
                TagsLibrarySection(
                    tags = state.tags,
                    searchQuery = state.searchQuery,
                    shouldShowAddTag = state.shouldShowAddTag,
                    palette = palette,
                    sectionShape = sectionShape,
                    sectionBorderColor = sectionBorderColor,
                    onCreateTagClick = {
                        focusManager.clearFocus()
                        onCreateTagClick()
                    },
                    onTagSortClick = onTagSortClick,
                    onTagClick = onTagClick,
                    onTagLongClick = onTagLongClick,
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}

@Composable
private fun BackgroundDecoration() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
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
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagsHeader(
    state: TagsPageUiState,
    selectedTagsCount: Int,
    sectionBorderColor: Color,
    onSelectedTagsClearClick: () -> Unit,
) {
    val palette = LocalCuiPalette.current

    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        ReviewCreationPageHeader(
            title = stringResource(R.string.reviewcreation_tags_title),
            productPictureUri = state.mainPictureUri,
            productName = state.productName,
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

            if (state.recommendedTags.isNotEmpty()) {
                TagsStatPill(
                    text = stringResource(R.string.reviewcreation_tags_badge_recommended, state.recommendedTags.size),
                    backgroundColor = Color(0xFFFFE3D2),
                    borderColor = Color.White.copy(alpha = 0.6F),
                    textColor = Color(0xFFD55A2B),
                )
            }
        }
    }
}

@Composable
private fun SearchSection(
    modifier: Modifier = Modifier,
    searchQuery: String,
    sectionShape: RoundedCornerShape,
    sectionBorderColor: Color,
    onSearchQueryInput: (String) -> Unit,
    onSearchQueryClearClick: () -> Unit,
) {
    val palette = LocalCuiPalette.current

    Box(
        modifier = modifier
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
        TagsSearchField(
            searchQuery = searchQuery,
            onSearchQueryInput = onSearchQueryInput,
            onSearchQueryClearClick = onSearchQueryClearClick,
        )
    }
}

@Composable
@ScreenPreview
private fun TagsScreenPreview() = CheckieLiteTheme {
    TagsScreen(
        state = mockUiState.tagsState,
    )
}
