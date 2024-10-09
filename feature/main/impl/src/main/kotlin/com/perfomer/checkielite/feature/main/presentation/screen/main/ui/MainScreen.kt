package com.perfomer.checkielite.feature.main.presentation.screen.main.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.modifier.bottomStrokeOnScroll
import com.perfomer.checkielite.common.ui.cui.widget.block.CuiBlock
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiFloatingActionButton
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.cui.widget.cell.CuiReviewHorizontalItem
import com.perfomer.checkielite.common.ui.cui.widget.cell.ReviewItem
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChip
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.pxToDp
import com.perfomer.checkielite.common.ui.util.span.appNameSpannable
import com.perfomer.checkielite.feature.main.R
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.MainUiState
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.Tag
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun MainScreen(
    state: MainUiState,
    onSettingsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onReviewClick: (id: String) -> Unit = {},
    onTagClick: (id: String) -> Unit = {},
    onFabClick: () -> Unit = {},
) {
    val scrollState = rememberLazyListState()

    Scaffold(
        floatingActionButton = {
            if (state !is MainUiState.Error) {
                CuiFloatingActionButton(
                    painter = painterResource(id = CommonDrawable.ic_plus),
                    contentDescription = stringResource(R.string.main_add_checkie),
                    onClick = onFabClick,
                    modifier = Modifier.imePadding()
                )
            }
        },
        topBar = {
            TopAppBar(
                scrollableState = scrollState,
                onSettingsClick = onSettingsClick,
                onSearchClick = onSearchClick,
            )
        },
    ) { contentPadding ->
        when (state) {
            is MainUiState.Loading -> Loading()

            is MainUiState.Content -> Content(
                state = state,
                scrollState = scrollState,
                contentPadding = contentPadding,
                onSearchClick = onSearchClick,
                onReviewClick = onReviewClick,
                onTagClick = onTagClick,
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
    onSearchClick: () -> Unit = {},
    onReviewClick: (id: String) -> Unit,
    onTagClick: (id: String) -> Unit,
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
            SearchField(onSearchClick = onSearchClick)
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (state.tags.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                TagsRow(tags = state.tags, onTagClick = onTagClick)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        items(
            items = state.reviews,
            key = { item -> item.id },
        ) { item ->
            CuiReviewHorizontalItem(item, onReviewClick)
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
        illustrationPainter = painterResource(CommonDrawable.ill_empty),
        modifier = Modifier.padding(32.dp)
    )
}

@Composable
private fun Error() {
    CuiBlock(
        title = stringResource(CommonString.common_error_title),
        message = stringResource(R.string.main_error_description),
        illustrationPainter = painterResource(CommonDrawable.ill_error),
        modifier = Modifier.padding(32.dp)
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopAppBar(
    scrollableState: LazyListState,
    onSettingsClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    val shouldShowDivider by remember {
        derivedStateOf {
            scrollableState.canScrollBackward && (scrollableState.firstVisibleItemIndex > 0 || scrollableState.firstVisibleItemScrollOffset.pxToDp() > 4 )
        }
    }

    CenterAlignedTopAppBar(
        title = { Text(text = appNameSpannable(), fontSize = 20.sp) },
        actions = {
            val shouldShowSearchIcon by remember { derivedStateOf { scrollableState.firstVisibleItemIndex > 0 } }
            AnimatedVisibility(visible = shouldShowSearchIcon, enter = fadeIn(tween(250)), exit = fadeOut(tween(250))) {
                CuiIconButton(
                    painter = painterResource(CommonDrawable.ic_search),
                    onClick = onSearchClick,
                )
            }

            CuiIconButton(
                painter = painterResource(R.drawable.ic_settings),
                onClick = onSettingsClick,
            )
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
    onSearchClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, LocalCuiPalette.current.OutlineSecondary, RoundedCornerShape(16.dp))
            .clickable(onClick = onSearchClick)
            .padding(horizontal = 20.dp, vertical = 13.dp)
    ) {
        Text(
            text = stringResource(CommonString.common_search),
            fontSize = 14.sp,
            color = LocalCuiPalette.current.TextSecondary,
            modifier = Modifier.weight(1F)
        )

        Icon(
            painter = painterResource(id = CommonDrawable.ic_search),
            tint = LocalCuiPalette.current.IconSecondary,
            contentDescription = null,
        )
    }
}

@Composable
private fun TagsRow(
    tags: ImmutableList<Tag>,
    onTagClick: (id: String) -> Unit,
) {
    val shouldShowSecondRow = tags.size >= 10

    val firstRow = remember(tags) {
        if (shouldShowSecondRow) tags.filterIndexed { index, _ -> index % 2 == 0 }.toPersistentList()
        else tags
    }

    val secondRow = remember(tags) {
        if (shouldShowSecondRow) tags.filterIndexed { index, _ -> index % 2 != 0 }.toPersistentList()
        else null
    }

    @Composable
    fun SingleRow(tags: ImmutableList<Tag>) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (tag in tags) {
                CuiChip(
                    leadingIcon = tag.emoji?.let { { Text(it) } },
                    onClick = { onTagClick(tag.id) },
                    content = { Text(tag.value) },
                )
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        SingleRow(tags = firstRow)

        if (secondRow != null) {
            SingleRow(tags = secondRow)
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
    ),
    tags = emptyPersistentList(),
)