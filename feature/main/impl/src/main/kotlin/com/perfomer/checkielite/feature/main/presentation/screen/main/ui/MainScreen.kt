package com.perfomer.checkielite.feature.main.presentation.screen.main.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gigamole.composefadingedges.FadingEdgesGravity
import com.gigamole.composefadingedges.horizontalFadingEdges
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.modifier.bottomStrokeOnScroll
import com.perfomer.checkielite.common.ui.cui.widget.block.CuiBlock
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiFloatingActionButton
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedFieldDefaults
import com.perfomer.checkielite.common.ui.cui.widget.rating.ReviewRating
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.main.R
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.MainUiState
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.ReviewItem
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
internal fun MainScreen(
    state: MainUiState,
    onSearchQueryInput: (query: String) -> Unit = {},
    onSearchQueryClearClick: () -> Unit = {},
    onReviewClick: (id: String) -> Unit = {},
    onFabClick: () -> Unit = {},
) {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val searchFieldFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Scaffold(
        floatingActionButton = {
            CuiFloatingActionButton(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = stringResource(R.string.main_add_checkie),
                onClick = onFabClick,
                modifier = Modifier.imePadding()
            )
        },
        topBar = {
            TopAppBar(
                scrollableState = scrollState,
                onSearchClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(0)
                        searchFieldFocusRequester.requestFocus()
                    }
                },
            )
        },
    ) { contentPadding ->
        when (state) {
            is MainUiState.Loading -> Loading()

            is MainUiState.Content -> Content(
                state = state,
                scrollState = scrollState,
                contentPadding = contentPadding,
                searchFieldFocusRequester = searchFieldFocusRequester,
                onSearchQueryInput = onSearchQueryInput,
                onSearchQueryClearClick = {
                    focusManager.clearFocus()
                    onSearchQueryClearClick()
                },
                onReviewClick = onReviewClick,
            )

            is MainUiState.Empty -> Empty()

            is MainUiState.Error -> Error()
        }
    }
}

@Composable
private fun Content(
    state: MainUiState.Content,
    scrollState: LazyListState,
    contentPadding: PaddingValues,
    searchFieldFocusRequester: FocusRequester,
    onSearchQueryInput: (query: String) -> Unit,
    onSearchQueryClearClick: () -> Unit,
    onReviewClick: (id: String) -> Unit,
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
                onSearchQueryInput = onSearchQueryInput,
                onSearchQueryClearClick = onSearchQueryClearClick,
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        itemsIndexed(
            items = state.reviews,
            key = { _, item -> item.id },
        ) { _, item ->
            CheckieHorizontalItem(item, onReviewClick)
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
private fun Loading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Empty() {
    CuiBlock(
        title = stringResource(R.string.main_empty_title),
        message = stringResource(R.string.main_empty_description),
        illustrationPainter = painterResource(CommonDrawable.ill_empty)
    )
}

@Composable
private fun Error() {
    CuiBlock(
        title = stringResource(CommonString.common_error_title),
        message = stringResource(CommonString.common_error_message),
        illustrationPainter = painterResource(CommonDrawable.ill_error),
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopAppBar(
    scrollableState: LazyListState,
    onSearchClick: () -> Unit,
) {
    val shouldShowDivider by remember { derivedStateOf { scrollableState.canScrollBackward } }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = buildAnnotatedString {
                    append(
                        AnnotatedString(
                            text = stringResource(id = R.string.app_name_checkie),
                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                        )
                    )
                    append(" ")
                    append(
                        AnnotatedString(
                            text = stringResource(id = R.string.app_name_lite),
                            spanStyle = SpanStyle(fontWeight = FontWeight.Normal)
                        )
                    )
                },
                fontSize = 20.sp,
            )
        },
        actions = {
            val shouldShowSearchIcon by remember { derivedStateOf { scrollableState.firstVisibleItemIndex > 0 } }
            AnimatedVisibility(visible = shouldShowSearchIcon, enter = fadeIn(tween(250)), exit = fadeOut(tween(250))) {
                CuiIconButton(
                    painter = painterResource(R.drawable.ic_search),
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

@Composable
private fun SearchField(
    searchQuery: String,
    focusRequester: FocusRequester,
    onSearchQueryInput: (query: String) -> Unit,
    onSearchQueryClearClick: () -> Unit,
) {
    CuiOutlinedField(
        text = searchQuery,
        placeholder = stringResource(R.string.main_search),
        trailingIcon = {
            if (searchQuery.isBlank()) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    tint = LocalCuiPalette.current.IconSecondary,
                    contentDescription = null,
                )
            } else {
                CuiIconButton(
                    painter = painterResource(id = CommonDrawable.ic_cross),
                    contentDescription = stringResource(R.string.main_clear),
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
internal fun CheckieHorizontalItem(
    item: ReviewItem,
    onClick: (id: String) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(item.id) }
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(LocalCuiPalette.current.BackgroundSecondary)
        ) {
            if (item.imageUri != null) {
                AsyncImage(
                    model = item.imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = painterResource(id = CommonDrawable.ic_image),
                    tint = LocalCuiPalette.current.IconTertiary,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }

            SyncingBlock(item.isSyncing)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1F)) {
            Text(
                text = item.title,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Visible,
                softWrap = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalFadingEdges(gravity = FadingEdgesGravity.End, length = 40.dp)
            )

            if (item.brand != null) {
                Text(
                    text = item.brand.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = LocalCuiPalette.current.TextAccent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalFadingEdges(gravity = FadingEdgesGravity.End, length = 40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        ReviewRating(rating = item.rating)
    }
}

@Composable
private fun SyncingBlock(isSyncing: Boolean) {
    AnimatedVisibility(visible = isSyncing, enter = fadeIn(), exit = fadeOut()) {
        Box(
            modifier = Modifier
                .background(LocalCuiPalette.current.BackgroundPrimary.copy(alpha = 0.5F))
                .padding(12.dp)
        ) {
            CircularProgressIndicator(
                color = LocalCuiPalette.current.IconPrimary,
                strokeWidth = 2.dp,
            )
        }
    }
}

@ScreenPreview
@Composable
private fun MainScreenContentPreview() = CheckieLiteTheme {
    MainScreen(state = mockUiState)
}

@ScreenPreview
@Composable
private fun MainScreenErrorPreview() = CheckieLiteTheme {
    MainScreen(state = MainUiState.Error)
}

internal val mockUiState = MainUiState.Content(
    searchQuery = "",
    reviews = persistentListOf(
        ReviewItem(
            id = "1",
            title = "Chicken toasts with poached eggs",
            brand = "Lui Bidon",
            imageUri = "https://habrastorage.org/r/w780/getpro/habr/upload_files/746/2ab/27c/7462ab27cca552ce31ee9cba01387692.jpeg",
            rating = 8,
            isSyncing = false,
        ),
        ReviewItem(
            id = "2",
            title = "Lemonblast",
            brand = "Darkside",
            imageUri = "https://images.unsplash.com/photo-1483129804960-cb1964499894?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
            rating = 0,
            isSyncing = false,
        ),
        ReviewItem(
            id = "3",
            title = "One Flew Over the Cuckoos Next",
            brand = "Key Kesey",
            imageUri = "https://images.unsplash.com/photo-1620447875063-19be4e4604bc?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=796&q=80",
            rating = 10,
            isSyncing = false,
        ),
        ReviewItem(
            id = "4",
            title = "The Wolf of Wall Street",
            brand = null,
            imageUri = "https://images.unsplash.com/photo-1548100535-fe8a16c187ef?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1151&q=80",
            rating = 4,
            isSyncing = false,
        ),
        ReviewItem(
            id = "5",
            title = "My own dog",
            brand = null,
            imageUri = null,
            rating = 3,
            isSyncing = false,
        ),
    )
)