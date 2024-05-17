package com.perfomer.checkielite.feature.search.presentation.screen.search.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.modifier.bottomStrokeOnScroll
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.cui.widget.cell.CuiReviewHorizontalItem
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChip
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChipStyle
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.search.R
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.SearchUiState
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.widget.OutlinedSearchField

@Composable
internal fun SearchScreen(
    state: SearchUiState,
    onNavigationIconClick: () -> Unit = {},
    onSearchFieldInput: (text: String) -> Unit = {},
    onSearchClearClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onTagFilterClearClick: (tagId: String) -> Unit = {},
    onRatingRangeFilterClearClick: () -> Unit = {},
    onReviewClick: (id: String) -> Unit = {},
    onRecentSearchesClearClick: () -> Unit = {},
) {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val searchFieldFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        searchFieldFocusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            SearchTopAppBar(
                scrollableState = scrollState,
                onNavigationIconClick = onNavigationIconClick,
                searchFieldFocusRequester = searchFieldFocusRequester,
                searchQuery = state.searchQuery,
                onSearchFieldInput = onSearchFieldInput,
                onSearchClearClick = {
                    focusManager.clearFocus()
                    onSearchClearClick()
                },
            )
        },
    ) { contentPadding ->
        Content(
            state = state,
            scrollState = scrollState,
            contentPadding = contentPadding,

            onReviewClick = onReviewClick,
            onRatingRangeFilterClearClick = onRatingRangeFilterClearClick,
            onRecentSearchesClearClick = onRecentSearchesClearClick,
            onTagFilterClearClick = onTagFilterClearClick,
            onSortClick = onSortClick,
            onFilterClick = onFilterClick,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Content(
    state: SearchUiState,
    scrollState: LazyListState,
    contentPadding: PaddingValues,
    onSortClick: () -> Unit,
    onFilterClick: () -> Unit,
    onTagFilterClearClick: (tagId: String) -> Unit,
    onRatingRangeFilterClearClick: () -> Unit,
    onReviewClick: (id: String) -> Unit,
    onRecentSearchesClearClick: () -> Unit,
) {
    LazyColumn(
        contentPadding = contentPadding,
        state = scrollState,
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))



            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                CuiChip(
                    onClick = onSortClick,
                    leadingIcon = { Icon(painter = painterResource(R.drawable.ic_sort), contentDescription = null) },
                    content = { Text(stringResource(R.string.search_sort)) },
                )

                CuiChip(
                    onClick = onFilterClick,
                    leadingIcon = { Icon(painter = painterResource(R.drawable.ic_filter), contentDescription = null) },
                    content = { Text(stringResource(R.string.search_filter)) },
                )
            }
        }

        item {
            FlowRow(modifier = Modifier.fillMaxWidth()) {
                if (state.ratingFilter != null) {
                    CuiChip(
                        style = CuiChipStyle.selected(),
                        onClick = onFilterClick,
                        content = {
                            Row {
                                Text(
                                    text = buildAnnotatedString {
                                        append(stringResource(R.string.search_filter_rating))
                                        append(": ")
                                        append(
                                            AnnotatedString(
                                                text = "${state.ratingFilter.minRating}-${state.ratingFilter.maxRating}",
                                                spanStyle = SpanStyle(fontWeight = FontWeight.Medium),
                                            )
                                        )
                                    }
                                )

                                Spacer(Modifier.width(8.dp))

                                CuiIconButton(
                                    painter = painterResource(CommonDrawable.ic_cross),
                                    onClick = onRatingRangeFilterClearClick,
                                    tint = LocalCuiPalette.current.IconAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        },
                    )
                }

                for (tagFilter in state.tagFilters) {
                    key(tagFilter.tagId) {
                        CuiChip(
                            style = CuiChipStyle.selected(),
                            onClick = onFilterClick,
                            leadingIcon = tagFilter.emoji?.let { { Text(tagFilter.emoji) } },
                            content = {
                                Row {
                                    Text(tagFilter.value)

                                    Spacer(Modifier.width(8.dp))

                                    CuiIconButton(
                                        painter = painterResource(CommonDrawable.ic_cross),
                                        onClick = { onTagFilterClearClick(tagFilter.tagId) },
                                        tint = LocalCuiPalette.current.IconAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            },
                        )
                    }
                }
            }
        }

        itemsIndexed(
            items = state.reviews,
            key = { _, item -> item.id },
        ) { _, item ->
            CuiReviewHorizontalItem(item, onReviewClick)
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
    onSearchFieldInput: (text: String) -> Unit,
    onSearchClearClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
) {
    val shouldShowDivider by remember { derivedStateOf { scrollableState.canScrollBackward } }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
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
    }
}

@ScreenPreview
@Composable
private fun SearchScreenPreview() = CheckieLiteTheme {
    SearchScreen(state = mockUiState)
}

internal val mockUiState = SearchUiState(
    searchQuery = "",
    ratingFilter = null,
    tagFilters = emptyPersistentList(),
    reviews = emptyPersistentList(),
    showRecentSearchesTitle = false,
)