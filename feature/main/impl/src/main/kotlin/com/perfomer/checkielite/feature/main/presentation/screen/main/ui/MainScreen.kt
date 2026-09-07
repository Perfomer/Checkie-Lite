package com.perfomer.checkielite.feature.main.presentation.screen.main.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.isRenderEffectSupported
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.modifier.softShadow
import com.perfomer.checkielite.common.ui.cui.modifier.thenIf
import com.perfomer.checkielite.common.ui.cui.widget.block.CuiBlock
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiFloatingActionButton
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.cui.widget.cell.CuiReviewCard
import com.perfomer.checkielite.common.ui.cui.widget.cell.ReviewItem
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChipStyle
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiTagChip
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiGlassToolbarBackground
import com.perfomer.checkielite.common.ui.presentation.theme.CuiSurfaceContent
import com.perfomer.checkielite.common.ui.presentation.theme.CuiSurfaceStyle
import com.perfomer.checkielite.common.ui.presentation.transition.isSharedTransitionItemEligible
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.LocalLiquidGlassEnabled
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.app.appNameSpannable
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.feature.main.R
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.MainUiState
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.Tag
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.WhatsNewBanner
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.widget.ChangelogBanner
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.widget.MainHeaderBackground
import com.perfomer.checkielite.feature.main.presentation.util.TagRowUiBalancer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun MainScreen(
    state: MainUiState,
    onSettingsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onReviewClick: (id: String) -> Unit = {},
    onTagClick: (id: String) -> Unit = {},
    onChangelogClick: () -> Unit = {},
    onChangelogCloseClick: () -> Unit = {},
    onFabClick: () -> Unit = {},
) = CuiSurfaceContent {
    val scrollState = rememberLazyListState()
    val backgroundColor = CuiSurfaceStyle.background
    val backdrop = rememberLayerBackdrop {
        // Include the screen color in the gaps between cards when sampling the list.
        drawRect(backgroundColor)
        drawContent()
    }

    Scaffold(
        containerColor = backgroundColor,
        floatingActionButton = {
            if (state !is MainUiState.Error) {
                CuiFloatingActionButton(
                    painter = painterResource(id = CommonDrawable.ic_plus),
                    contentDescription = stringResource(R.string.main_add_checkie),
                    onClick = onFabClick,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp,
                        hoveredElevation = 0.dp,
                    ),
                    modifier = Modifier
                        .imePadding()
                        .softShadow(shape = CircleShape, radius = 16.dp)
                )
            }
        },
        topBar = {
            if (state is MainUiState.Content) {
                TopAppBar(
                    onSettingsClick = onSettingsClick,
                    onSearchClick = onSearchClick,
                    scrollState = scrollState,
                    backgroundColor = backgroundColor,
                    backdrop = backdrop,
                )
            } else {
                MainHeaderBackground(content = { TopAppBar(onSettingsClick = onSettingsClick) })
            }
        },
    ) { contentPadding ->
        when (state) {
            is MainUiState.Loading -> Loading()

            is MainUiState.Content -> Content(
                state = state,
                scrollState = scrollState,
                contentPadding = contentPadding,
                backdrop = backdrop,
                onSearchClick = onSearchClick,
                onReviewClick = onReviewClick,
                onTagClick = onTagClick,
                onChangelogClick = onChangelogClick,
                onChangelogCloseClick = onChangelogCloseClick,
            )

            is MainUiState.Empty -> Box(modifier = Modifier.padding(contentPadding)) { Empty() }

            is MainUiState.Error -> Box(modifier = Modifier.padding(contentPadding)) { Error() }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Content(
    state: MainUiState.Content,
    scrollState: LazyListState,
    contentPadding: PaddingValues,
    backdrop: LayerBackdrop,
    onSearchClick: () -> Unit = {},
    onReviewClick: (id: String) -> Unit,
    onTagClick: (id: String) -> Unit,
    onChangelogClick: () -> Unit,
    onChangelogCloseClick: () -> Unit,
) {
    val density = LocalDensity.current
    val toolbarBottom = with(density) { contentPadding.calculateTopPadding().roundToPx() }

    LazyColumn(
        // Keep the decoration behind the pinned toolbar until the header scrolls away.
        contentPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding()),
        state = scrollState,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .thenIf(LocalLiquidGlassEnabled.current && isRenderEffectSupported()) { layerBackdrop(backdrop) }
    ) {
        item(key = "header") {
            MainHeaderBackground(
                content = {
                    Column(modifier = Modifier.padding(top = contentPadding.calculateTopPadding())) {
                        SearchField(
                            onSearchClick = onSearchClick,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )

                        if (state.tags.isNotEmpty()) {
                            TagsRow(
                                tags = state.tags,
                                onTagClick = onTagClick,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        state.whatsNewBanner?.let { banner ->
            item(key = "changelog") {
                ChangelogBanner(
                    state = banner,
                    onClick = onChangelogClick,
                    onCloseClick = onChangelogCloseClick,
                    modifier = Modifier
                        .animateItem()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 8.dp)
                )
            }
        }

        items(
            items = state.reviews,
            key = { item -> item.id },
        ) { item ->
            CuiReviewCard(
                item = item,
                onClick = onReviewClick,
                isTransitionEnabled = {
                    val layout = scrollState.layoutInfo
                    val card = layout.visibleItemsInfo.firstOrNull { it.key == item.id }
                    isSharedTransitionItemEligible(
                        totalItemsCount = layout.totalItemsCount,
                        itemOffset = card?.offset,
                        itemSize = card?.size,
                        viewportStartOffset = toolbarBottom,
                        viewportEndOffset = layout.viewportEndOffset,
                    )
                },
                modifier = Modifier
                    .animateItem()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 12.dp)
            )
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
    onSettingsClick: () -> Unit,
    onSearchClick: () -> Unit = {},
    scrollState: LazyListState? = null,
    backgroundColor: Color = Color.Transparent,
    backdrop: Backdrop? = null,
) {
    val searchScrollThreshold = with(LocalDensity.current) { 56.dp.toPx() }
    val showSearch by remember(scrollState, searchScrollThreshold) {
        derivedStateOf {
            scrollState != null && (
                scrollState.firstVisibleItemIndex > 0 ||
                    scrollState.firstVisibleItemScrollOffset >= searchScrollThreshold
                )
        }
    }

    val showBackground by remember(scrollState) {
        derivedStateOf {
            scrollState != null && (
                scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 0
                )
        }
    }
    val backgroundScrollThreshold = with(LocalDensity.current) { 24.dp.toPx() }

    Box {
        if (showBackground && scrollState != null && backdrop != null) {
            CuiGlassToolbarBackground(
                backdrop = backdrop,
                backgroundColor = backgroundColor,
                progress = {
                    if (scrollState.firstVisibleItemIndex > 0) {
                        1F
                    } else {
                        (scrollState.firstVisibleItemScrollOffset / backgroundScrollThreshold).coerceIn(0F, 1F)
                    }
                },
                modifier = Modifier.matchParentSize()
            )
        }

        CenterAlignedTopAppBar(
            title = { Text(text = appNameSpannable(), fontSize = 20.sp) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            actions = {
                AnimatedVisibility(
                    visible = showSearch,
                    enter = fadeIn(tween(200)),
                    exit = fadeOut(tween(200)),
                ) {
                    CuiIconButton(
                        painter = painterResource(CommonDrawable.ic_search),
                        contentDescription = stringResource(CommonString.common_search),
                        onClick = onSearchClick,
                    )
                }

                CuiIconButton(
                    painter = painterResource(R.drawable.ic_settings),
                    onClick = onSettingsClick,
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SearchField(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalCuiPalette.current
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        onClick = onSearchClick,
        interactionSource = interactionSource,
        shape = CircleShape,
        color = palette.BackgroundElevationBase,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .softShadow(interactionSource = interactionSource, shape = CircleShape)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .heightIn(min = 48.dp)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Icon(
                painter = painterResource(id = CommonDrawable.ic_search),
                tint = palette.IconSecondary,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = stringResource(CommonString.common_search),
                fontSize = 16.sp,
                color = palette.TextSecondary,
                modifier = Modifier.weight(1F)
            )
        }
    }
}

@Composable
private fun TagsRow(
    tags: ImmutableList<Tag>,
    onTagClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val rows = remember(tags) { TagRowUiBalancer.split(tags) }
    val palette = LocalCuiPalette.current
    val chipStyle = CuiChipStyle.elevated(palette)

    @Composable
    fun SingleRow(tags: ImmutableList<Tag>) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (tag in tags) {
                val interactionSource = remember(tag.id) { MutableInteractionSource() }

                CuiTagChip(
                    text = tag.value,
                    emoji = tag.emoji,
                    onClick = { onTagClick(tag.id) },
                    style = chipStyle,
                    interactionSource = interactionSource,
                    modifier = Modifier.softShadow(
                        interactionSource = interactionSource,
                        shape = CircleShape,
                        radius = 8.dp,
                        offset = DpOffset(x = 0.dp, y = 2.dp),
                    )
                )
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        SingleRow(tags = rows.first)

        rows.second?.let { SingleRow(tags = it) }
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
    tags = persistentListOf(
        Tag(id = "restaurants", value = "Рестораны", emoji = "🍴"),
        Tag(id = "food", value = "Еда", emoji = "🥗"),
        Tag(id = "drinks", value = "Напитки", emoji = "🥤"),
        Tag(id = "sweets", value = "Сладкое", emoji = "🍬"),
        Tag(id = "city", value = "Красноярск", emoji = "🏙️"),
        Tag(id = "snacks", value = "Снеки", emoji = "🍟"),
    ),
    whatsNewBanner = WhatsNewBanner(
        title = Text.raw("What’s new in 1.6.0"),
    ),
)
