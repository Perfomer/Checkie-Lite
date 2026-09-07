package com.perfomer.checkielite.feature.search.presentation.screen.search.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.cui.modifier.softShadow
import com.perfomer.checkielite.common.ui.cui.modifier.toolbarDivider
import com.perfomer.checkielite.common.ui.cui.widget.block.CuiBlock
import com.perfomer.checkielite.common.ui.cui.widget.cell.CuiReviewCard
import com.perfomer.checkielite.common.ui.cui.widget.cell.ReviewItem
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiGlassScaffold
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.add
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.common.ui.util.resource.text.text
import com.perfomer.checkielite.feature.search.R
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.Filter
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.Filter.FilterType
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.SearchContentType
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.SearchUiState
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.widget.SearchField
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun SearchScreen(
    state: SearchUiState,
    searchFieldFocusRequester: FocusRequester = remember { FocusRequester() },
    onNavigationIconClick: () -> Unit = {},
    onSearchFieldInput: (text: String) -> Unit = {},
    onSearchClearClick: () -> Unit = {},
    onFilterClick: (type: FilterType) -> Unit = {},
    onReviewClick: (id: String) -> Unit = {},
    onRecentSearchesClearClick: () -> Unit = {},
) {
    val scrollState = rememberLazyListState()
    val isDragging by scrollState.interactionSource.collectIsDraggedAsState()
    val focusManager = LocalFocusManager.current
    val palette = LocalCuiPalette.current
    val backgroundColor = lerp(palette.BackgroundPrimary, palette.BackgroundAccentTertiary, 0.4F)
    val backgroundScrollThreshold = with(LocalDensity.current) { 24.dp.toPx() }

    LaunchedEffect(isDragging) {
        if (isDragging) focusManager.clearFocus()
    }

    UpdateEffect(state.filters to state.searchQuery) {
        scrollState.scrollToItem(0)
    }

    CuiGlassScaffold(
        containerColor = backgroundColor,
        toolbarColor = backgroundColor,
        toolbarBackgroundProgress = {
            if (scrollState.firstVisibleItemIndex > 0) {
                1F
            } else {
                (scrollState.firstVisibleItemScrollOffset / backgroundScrollThreshold).coerceIn(0F, 1F)
            }
        },
        topBar = {
            SearchTopAppBar(
                scrollState = scrollState,
                searchFieldFocusRequester = searchFieldFocusRequester,
                searchQuery = state.searchQuery,
                filters = state.filters,
                onNavigationIconClick = onNavigationIconClick,
                onSearchFieldInput = onSearchFieldInput,
                onSearchClearClick = onSearchClearClick,
                onFilterClick = { filterType ->
                    focusManager.clearFocus()
                    onFilterClick(filterType)
                },
            )
        },
    ) { contentPadding ->
        if (state.reviews.isEmpty()) {
            Empty(contentType = state.contentType, scrollState = scrollState, contentPadding = contentPadding)
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
        contentPadding = contentPadding.add(start = 20.dp, top = 4.dp, end = 20.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        state = scrollState,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        item(key = "section", contentType = "section") {
            ResultsTitle(
                contentType = state.contentType,
                count = state.reviews.size,
                onRecentSearchesClearClick = onRecentSearchesClearClick,
            )
        }

        items(
            items = state.reviews,
            key = { item -> item.id },
            contentType = { "review" },
        ) { item ->
            CuiReviewCard(
                item = item,
                onClick = onReviewClick,
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
private fun Empty(
    contentType: SearchContentType,
    scrollState: LazyListState,
    contentPadding: PaddingValues,
) {
    val isRecent = contentType == SearchContentType.RECENT_SEARCHES

    // A scrollable empty state remains readable with the keyboard and large system fonts.
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(contentPadding)
    ) {
        val availableHeight = maxHeight
        LazyColumn(state = scrollState, modifier = Modifier.fillMaxSize()) {
            item {
                CuiBlock(
                    title = stringResource(if (isRecent) R.string.search_empty_start_title else R.string.search_empty_title),
                    message = stringResource(
                        if (isRecent) R.string.search_empty_start_description else R.string.search_empty_description,
                    ),
                    illustrationPainter = painterResource(CommonDrawable.ill_empty),
                    modifier = Modifier
                        .heightIn(min = availableHeight)
                        .padding(horizontal = 32.dp, vertical = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchTopAppBar(
    searchFieldFocusRequester: FocusRequester,
    scrollState: LazyListState,
    searchQuery: String,
    filters: ImmutableList<Filter>,
    onSearchFieldInput: (text: String) -> Unit,
    onSearchClearClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    onFilterClick: (type: FilterType) -> Unit,
) {
    val shouldShowDivider by remember(scrollState) { derivedStateOf { scrollState.canScrollBackward } }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .toolbarDivider(
                show = shouldShowDivider,
                strokeColor = LocalCuiPalette.current.OutlineSecondary,
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 6.dp)
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
                    .weight(1F)
                    .padding(start = 4.dp, end = 20.dp)
                    .focusRequester(searchFieldFocusRequester)
            )
        }

        FiltersRow(filters = filters, onFilterClick = onFilterClick)
    }
}

@Composable
private fun FiltersRow(
    filters: ImmutableList<Filter>,
    onFilterClick: (type: FilterType) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        filters.forEach { filter ->
            key(filter.type) {
                FilterChip(filter = filter, onClick = { onFilterClick(filter.type) })
            }
        }
    }
}

@Composable
private fun FilterChip(
    filter: Filter,
    onClick: () -> Unit,
) {
    val palette = LocalCuiPalette.current
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = CircleShape,
        color = if (filter.isApplied) palette.BackgroundAccentSecondary else palette.BackgroundElevationBase,
        contentColor = if (filter.isApplied) palette.TextAccent else palette.TextPrimary,
        modifier = Modifier
            .semantics { selected = filter.isApplied }
            .softShadow(
                interactionSource = interactionSource,
                shape = CircleShape,
                radius = 8.dp,
                offset = DpOffset(x = 0.dp, y = 2.dp),
            )
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .heightIn(min = 40.dp)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            filter.leadingIcon?.let { leadingIcon ->
                when (leadingIcon.type) {
                    Filter.LeadingIconType.EMOJI -> Text(text = leadingIcon.value, fontSize = 16.sp)
                    Filter.LeadingIconType.BADGE -> Surface(
                        color = palette.BackgroundAccentPrimary,
                        contentColor = palette.TextInverted,
                        shape = CircleShape,
                    ) {
                        Text(
                            text = leadingIcon.value,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            Text(
                text = text(filter.text),
                fontSize = 14.sp,
                fontWeight = if (filter.isApplied) FontWeight.Medium else FontWeight.Normal,
            )

            Icon(
                painter = painterResource(CommonDrawable.ic_chevron_down),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun ResultsTitle(
    contentType: SearchContentType,
    count: Int,
    onRecentSearchesClearClick: () -> Unit,
) {
    val isRecent = contentType == SearchContentType.RECENT_SEARCHES

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
    ) {
        Text(
            text = stringResource(if (isRecent) R.string.search_recent_searches else R.string.search_results),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = LocalCuiPalette.current.TextPrimary,
            modifier = Modifier.weight(1F)
        )

        if (isRecent) {
            TextButton(onClick = onRecentSearchesClearClick) {
                Text(text = stringResource(CommonString.common_clear), color = LocalCuiPalette.current.TextAccent)
            }
        } else {
            Text(text = count.toString(), fontSize = 14.sp, color = LocalCuiPalette.current.TextSecondary)
        }
    }
}

@ScreenPreview
@Composable
private fun SearchScreenRecentPreview() = CheckieLiteTheme {
    SearchScreen(state = mockUiState)
}

@ScreenPreview
@Composable
private fun SearchScreenResultsPreview() = CheckieLiteTheme {
    SearchScreen(state = mockUiState.copy(searchQuery = "Lemon", contentType = SearchContentType.CURRENT_SEARCH))
}

@ScreenPreview
@Composable
private fun SearchScreenDarkPreview() = CheckieLiteTheme(darkTheme = true) {
    SearchScreen(state = mockUiState)
}

@ScreenPreview
@Composable
private fun SearchScreenEmptyPreview() = CheckieLiteTheme {
    SearchScreen(state = mockUiState.copy(reviews = persistentListOf()))
}

@ScreenPreview
@Composable
private fun SearchScreenNoResultsPreview() = CheckieLiteTheme {
    SearchScreen(
        state = mockUiState.copy(
            searchQuery = "Lemon",
            reviews = persistentListOf(),
            contentType = SearchContentType.CURRENT_SEARCH,
        ),
    )
}

internal val mockUiState = SearchUiState(
    searchQuery = "",
    filters = persistentListOf(
        Filter(type = FilterType.TAGS, text = Text.resource(R.string.search_filters_tags), isApplied = false),
        Filter(type = FilterType.SORT, text = Text.resource(R.string.search_sort_relevant), isApplied = true),
    ),
    reviews = persistentListOf(
        ReviewItem(id = "1", title = "Lemon tart", brand = "Lui Bidon", imageUri = null, rating = 10, isSyncing = false),
        ReviewItem(id = "2", title = "Strawberry lemonade", brand = "Coffee shop", imageUri = null, rating = 8, isSyncing = false),
        ReviewItem(id = "3", title = "A very long product name that wraps onto another line", brand = null, imageUri = null, rating = 0, isSyncing = false),
    ),
    contentType = SearchContentType.RECENT_SEARCHES,
)
