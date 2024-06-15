package com.perfomer.checkielite.feature.search.presentation.screen.search.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.modifier.bottomStrokeOnScroll
import com.perfomer.checkielite.common.ui.cui.widget.block.CuiBlock
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiOutlineButton
import com.perfomer.checkielite.common.ui.cui.widget.cell.CuiReviewHorizontalItem
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChipStyle
import com.perfomer.checkielite.common.ui.cui.widget.scrim.HorizontalScrim
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.copy
import com.perfomer.checkielite.feature.search.R
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.Filter
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.Filter.FilterType
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.SearchContentType
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.SearchUiState
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.widget.OutlinedSearchField
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun SearchScreen(
    state: SearchUiState,
    searchFieldFocusRequester: FocusRequester = remember { FocusRequester() },
    onNavigationIconClick: () -> Unit = {},
    onSearchFieldInput: (text: String) -> Unit = {},
    onSearchClearClick: () -> Unit = {},
    onFilterClick: (type: FilterType) -> Unit = {},
    onReviewClick: (id: String) -> Unit = {},
    onAllFiltersClick: () -> Unit = {},
    onClearAllFiltersClick: () -> Unit = {},
    onRecentSearchesClearClick: () -> Unit = {},
) {
    val scrollState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            SearchTopAppBar(
                scrollableState = scrollState,
                searchFieldFocusRequester = searchFieldFocusRequester,
                searchQuery = state.searchQuery,
                filters = state.filters,
                onNavigationIconClick = onNavigationIconClick,
                onSearchFieldInput = onSearchFieldInput,
                onFilterClick = { filterType ->
                    focusManager.clearFocus()
                    onFilterClick(filterType)
                },
                onAllFiltersClick = onAllFiltersClick,
                onSearchClearClick = {
                    focusManager.clearFocus()
                    onSearchClearClick()
                },
            )
        },
    ) { contentPadding ->
        if (state.reviews.isEmpty()) {
            Empty(
                contentType = state.contentType,
                contentPadding = contentPadding,
                onClearAllFiltersClick = onClearAllFiltersClick,
            )
        } else {
            Content(
                state = state,
                scrollState = scrollState,
                contentPadding = contentPadding,
                onReviewClick = onReviewClick,
                onRecentSearchesClearClick = onRecentSearchesClearClick,
            )
        }
    }
}

@Composable
private fun Content(
    state: SearchUiState,
    scrollState: LazyListState,
    contentPadding: PaddingValues,
    onReviewClick: (id: String) -> Unit,
    onRecentSearchesClearClick: () -> Unit,
) {
    LazyColumn(
        contentPadding = contentPadding.copy(
            top = contentPadding.calculateTopPadding() + 12.dp,
        ),
        state = scrollState,
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        if (state.contentType == SearchContentType.RECENT_SEARCHES) {
            item(
                key = "RecentSearches",
                contentType = "RecentSearches",
            ) {
                RecentSearchesTitle(onRecentSearchesClearClick = onRecentSearchesClearClick)
            }
        }

        items(
            items = state.reviews,
            key = { item -> item.id },
            contentType = { "Review" },
        ) { item ->
            CuiReviewHorizontalItem(item, onReviewClick)
        }
    }
}

@Composable
private fun Empty(
    contentType: SearchContentType,
    contentPadding: PaddingValues,
    onClearAllFiltersClick: () -> Unit = {},
) {
    when (contentType) {
        SearchContentType.RECENT_SEARCHES -> {
            CuiBlock(
                title = stringResource(R.string.search_empty_start_title),
                message = stringResource(R.string.search_empty_start_description),
                illustrationPainter = painterResource(CommonDrawable.ill_empty),
                modifier = Modifier
                    .imePadding()
                    .padding(contentPadding)
                    .padding(horizontal = 32.dp)
            )
        }
        SearchContentType.CURRENT_SEARCH -> {
            CuiBlock(
                title = stringResource(R.string.search_empty_title),
                message = stringResource(R.string.search_empty_description),
                illustrationPainter = painterResource(CommonDrawable.ill_empty),
                modifier = Modifier
                    .imePadding()
                    .padding(contentPadding)
                    .padding(horizontal = 32.dp)
            )
        }
    }
}

@Composable
private fun SearchField(
    searchQuery: String,
    onSearchQueryInput: (query: String) -> Unit,
    onSearchQueryClearClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedSearchField(
        value = searchQuery,
        onValueChange = onSearchQueryInput,
        placeholder = stringResource(CommonString.common_search),
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
        modifier = modifier
    )
}


@Composable
private fun SearchTopAppBar(
    searchFieldFocusRequester: FocusRequester,
    scrollableState: LazyListState,
    searchQuery: String,
    filters: ImmutableList<Filter>,
    onSearchFieldInput: (text: String) -> Unit,
    onSearchClearClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    onFilterClick: (type: FilterType) -> Unit,
    onAllFiltersClick: () -> Unit,
) {
    val shouldShowDivider by remember { derivedStateOf { scrollableState.canScrollBackward } }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalCuiPalette.current.BackgroundPrimary)
            .statusBarsPadding()
            .bottomStrokeOnScroll(
                show = shouldShowDivider,
                strokeColor = LocalCuiPalette.current.OutlineSecondary,
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            CuiToolbarNavigationIcon(
                painter = painterResource(CommonDrawable.ic_arrow_back),
                onBackPress = onNavigationIconClick,
            )

            SearchField(
                searchQuery = searchQuery,
                onSearchQueryInput = onSearchFieldInput,
                onSearchQueryClearClick = onSearchClearClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 16.dp)
                    .focusRequester(searchFieldFocusRequester)
            )
        }

        Spacer(Modifier.height(14.dp))

        FiltersRow(
            filters = filters,
            onFilterClick = onFilterClick,
            onAllFiltersClick = onAllFiltersClick,
        )

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun FiltersRow(
    filters: ImmutableList<Filter>,
    onFilterClick: (type: FilterType) -> Unit,
    onAllFiltersClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        val filtersScrollState = rememberScrollState()
        val appliedFiltersCount = remember(filters) { filters.count { it.isApplied } }
        val showAllFiltersChip = appliedFiltersCount > 1 && false // todo remove false after adding one more filter

        if (showAllFiltersChip) {
            Spacer(Modifier.width(20.dp))

            FilterChip(
                onClick = onAllFiltersClick,
                modifier = Modifier.fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_sort),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp)
                )

                Spacer(Modifier.width(4.dp))

                Badge(value = appliedFiltersCount.toString(), LocalCuiPalette.current.IconPrimary)
            }

            Spacer(Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .weight(1F)
                .height(IntrinsicSize.Min)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(filtersScrollState)
                    .padding(
                        start = if (showAllFiltersChip) 0.dp else 20.dp,
                        end = 20.dp
                    )
            ) {
                filters.forEach { filter ->
                    key(filter.type) {
                        val style = if (filter.isApplied) CuiChipStyle.selected() else CuiChipStyle.default()

                        FilterChip(
                            style = style,
                            onClick = { onFilterClick(filter.type) },
                        ) {
                            filter.leadingIcon?.let { leadingIcon ->
                                when (leadingIcon.type) {
                                    Filter.LeadingIconType.BADGE -> Badge(value = leadingIcon.value, LocalCuiPalette.current.IconAccent)
                                    Filter.LeadingIconType.EMOJI -> Text(text = leadingIcon.value)
                                }
                            }

                            Spacer(Modifier.width(4.dp))

                            Text(filter.text)

                            Spacer(Modifier.width(4.dp))

                            Icon(
                                painter = painterResource(CommonDrawable.ic_chevron_down),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = showAllFiltersChip && filtersScrollState.canScrollBackward,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                HorizontalScrim(
                    fromLeft = true,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(16.dp)
                )
            }
        }
    }
}

@Composable
private fun RecentSearchesTitle(
    onRecentSearchesClearClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.search_recent_searches),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1F)
        )

        CuiOutlineButton(
            text = stringResource(CommonString.common_clear),
            textColor = LocalCuiPalette.current.TextAccent,
            borderColor = Color.Transparent,
            trailingIcon = {
                Icon(
                    painter = painterResource(CommonDrawable.ic_cross),
                    contentDescription = null,
                    tint = LocalCuiPalette.current.IconAccent,
                    modifier = Modifier.size(16.dp)
                )
            },
            onClick = onRecentSearchesClearClick,
        )
    }
}

@Composable
private fun Badge(
    value: String,
    backgroundColor: Color,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(16.dp)
            .background(backgroundColor, CircleShape)
    ) {
        Text(
            text = value,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            color = LocalCuiPalette.current.TextInverted,
            style = MaterialTheme.typography.bodySmall.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FilterChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    style: CuiChipStyle = CuiChipStyle.default(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
    content: @Composable RowScope.() -> Unit,
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
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(contentPadding)
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides textStyle,
                content = { content() },
            )
        }
    }
}

@ScreenPreview
@Composable
private fun SearchScreenPreview() = CheckieLiteTheme {
    SearchScreen(state = mockUiState)
}

internal val mockUiState = SearchUiState(
    searchQuery = "",
    filters = emptyPersistentList(),
    reviews = emptyPersistentList(),
    contentType = SearchContentType.CURRENT_SEARCH,
)