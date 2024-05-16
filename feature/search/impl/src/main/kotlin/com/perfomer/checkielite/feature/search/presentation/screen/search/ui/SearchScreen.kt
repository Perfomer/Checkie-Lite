package com.perfomer.checkielite.feature.search.presentation.screen.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.modifier.bottomStrokeOnScroll
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.cui.widget.cell.CuiReviewHorizontalItem
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChip
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChipStyle
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedFieldDefaults
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.search.R
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.SearchUiState
import kotlinx.coroutines.launch

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

    Scaffold(
        topBar = {
            SearchTopAppBar(
                scrollableState = scrollState,
                onNavigationIconClick = onNavigationIconClick,
                onSearchClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(0)
                        searchFieldFocusRequester.requestFocus()
                    }
                },
            )
        },
    ) { contentPadding ->
        Content(
            state = state,
            scrollState = scrollState,
            contentPadding = contentPadding,
            searchFieldFocusRequester = searchFieldFocusRequester,
            onSearchFieldInput = onSearchFieldInput,
            onSearchClearClick = {
                focusManager.clearFocus()
                onSearchClearClick()
            },
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
    searchFieldFocusRequester: FocusRequester,
    onSearchFieldInput: (text: String) -> Unit,
    onSearchClearClick: () -> Unit,
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

            SearchField(
                searchQuery = state.searchQuery,
                focusRequester = searchFieldFocusRequester,
                onSearchQueryInput = onSearchFieldInput,
                onSearchQueryClearClick = onSearchClearClick,
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
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
    focusRequester: FocusRequester,
    onSearchQueryInput: (query: String) -> Unit,
    onSearchQueryClearClick: () -> Unit,
) {
    CuiOutlinedField(
        text = searchQuery,
        placeholder = stringResource(CommonString.common_search),
        trailingIcon = {
            if (searchQuery.isBlank()) {
                Icon(
                    painter = painterResource(id = CommonDrawable.ic_search),
                    tint = LocalCuiPalette.current.IconSecondary,
                    contentDescription = null,
                )
            } else {
                CuiIconButton(
                    painter = painterResource(id = CommonDrawable.ic_cross),
                    contentDescription = stringResource(CommonString.common_clear),
                    tint = LocalCuiPalette.current.IconPrimary,
                    onClick = onSearchQueryClearClick,
                    modifier = Modifier.offset(x = (-8).dp)
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            capitalization = KeyboardCapitalization.Sentences
        ),
        singleLine = true,
        onValueChange = onSearchQueryInput,
        colors = CuiOutlinedFieldDefaults.colors(
            unfocusedBorderColor = LocalCuiPalette.current.OutlineSecondary,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .focusRequester(focusRequester)
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SearchTopAppBar(
    scrollableState: LazyListState,
    onNavigationIconClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    val shouldShowDivider by remember { derivedStateOf { scrollableState.canScrollBackward } }

    TopAppBar(
        title = {
            Text(text = stringResource(CommonString.common_search))
        },
        navigationIcon = {
            CuiToolbarNavigationIcon(
                painter = painterResource(CommonDrawable.ic_arrow_back),
                onBackPress = onNavigationIconClick,
            )
        },
        actions = {
            val shouldShowSearchIcon by remember { derivedStateOf { scrollableState.firstVisibleItemIndex > 0 } }
            AnimatedVisibility(visible = shouldShowSearchIcon, enter = fadeIn(tween(250)), exit = fadeOut(tween(250))) {
                CuiIconButton(
                    painter = painterResource(CommonDrawable.ic_search),
                    tint = LocalCuiPalette.current.IconPrimary,
                    onClick = onSearchClick,
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .bottomStrokeOnScroll(
                show = shouldShowDivider,
                strokeColor = LocalCuiPalette.current.OutlineSecondary,
            )
    )
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