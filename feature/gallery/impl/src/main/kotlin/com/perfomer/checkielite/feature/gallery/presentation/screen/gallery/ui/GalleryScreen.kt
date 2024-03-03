@file:OptIn(ExperimentalFoundationApi::class)

package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mxalbert.zoomable.OverZoomConfig
import com.mxalbert.zoomable.Zoomable
import com.mxalbert.zoomable.rememberZoomableState
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.cui.modifier.indicatorOffsetForPage
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.PreviewTheme
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui.state.GalleryUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
internal fun GalleryScreen(
    state: GalleryUiState,
    onDismiss: () -> Unit = {},
    onNavigationIconClick: () -> Unit = {},
    onPageChange: (pageIndex: Int) -> Unit = {},
    onPagerClick: () -> Unit = {},
) {
    var backgroundAlpha by remember { mutableFloatStateOf(1F) }
    val systemUiController = rememberSystemUiController()

    UpdateEffect(state.isUiShown) { systemUiController.isSystemBarsVisible = state.isUiShown }
    UpdateEffect(backgroundAlpha < 1F) { systemUiController.isSystemBarsVisible = true }

    DisposableEffect(Unit) {
        systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = false)

        onDispose {
            // TODO: color by theme when dark theme will be done
            systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = true)
            systemUiController.isSystemBarsVisible = true
        }
    }

    Scaffold(
        topBar = {
            AnimatedVisibility(visible = state.isUiShown, enter = fadeIn(), exit = fadeOut()) {
                GalleryTopAppBar(
                    title = state.titleText,
                    onNavigationIconClick = onNavigationIconClick,
                )
            }
        },
        containerColor = GalleryPalette.BackgroundColor.copy(alpha = backgroundAlpha)
    ) {
        Box {
            val coroutineScope = rememberCoroutineScope()
            val mainPagerState = rememberPagerState(
                pageCount = { state.picturesUri.size },
                initialPage = state.currentPicturePosition,
            )

            MainHorizontalPager(
                pagerState = mainPagerState,
                picturesUri = state.picturesUri,
                onPageChange = onPageChange,
                onPagerClick = onPagerClick,
                onDismissProgressChange = { progress -> backgroundAlpha = 1 - progress },
                onDismiss = onDismiss,
            )

            AnimatedVisibility(
                visible = state.isUiShown,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                PreviewHorizontalPager(
                    mainPagerState = mainPagerState,
                    picturesUri = state.picturesUri,
                    onPictureClick = { page ->
                        coroutineScope.launch { mainPagerState.animateScrollToPage(page) }
                    },
                )
            }
        }
    }
}

@Composable
private fun PreviewHorizontalPager(
    mainPagerState: PagerState,
    picturesUri: ImmutableList<String>,
    onPictureClick: (position: Int) -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = { picturesUri.size },
        initialPage = mainPagerState.currentPage,
    )

    UpdateEffect(mainPagerState.currentPage) {
        coroutineScope { pagerState.animateScrollToPage(mainPagerState.currentPage) }
    }

    Column(
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    0F to Color.Transparent,
                    0.75F to GalleryPalette.BarColor,
                    1F to GalleryPalette.BarColor,
                )
            )
            .navigationBarsPadding()
    ) {
        Spacer(Modifier.height(72.dp))

        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(56.dp),
            pageSpacing = 8.dp,
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) { page ->
            val offset = mainPagerState.indicatorOffsetForPage(page)
            val targetAlpha = 0.5F + 0.5F * offset

            AsyncImage(
                model = picturesUri[page],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onPictureClick(page) }
                    .alpha(targetAlpha)
            )
        }
    }
}

@Composable
private fun MainHorizontalPager(
    pagerState: PagerState,
    picturesUri: ImmutableList<String>,
    onPageChange: (pageIndex: Int) -> Unit,
    onPagerClick: () -> Unit,
    onDismissProgressChange: (progress: Float) -> Unit,
    onDismiss: () -> Unit,
) {
    UpdateEffect(pagerState.currentPage) { onPageChange(pagerState.currentPage) }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val zoomableState = rememberZoomableState(
            minScale = 0.9F,
            maxScale = 6F,
            overZoomConfig = OverZoomConfig(1F, 4F),
        )

        UpdateEffect(zoomableState.dismissDragProgress) {
            onDismissProgressChange(zoomableState.dismissDragProgress.coerceIn(0F, 1F))
        }

        Zoomable(
            state = zoomableState,
            onTap = { onPagerClick() },
            onDismiss = { onDismiss(); true },
            dismissGestureEnabled = true,
        ) {
            MainGalleryPicture(pictureUri = picturesUri[page])
        }

        // Reset zoom state when the page is moved out of the window.
        val isVisible = page == pagerState.settledPage
        LaunchedEffect(isVisible) {
            if (!isVisible) {
                zoomableState.animateScaleTo(
                    targetScale = 1F,
                    animationSpec = tween(0),
                )
            }
        }
    }
}

@Composable
private fun MainGalleryPicture(pictureUri: String) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(pictureUri)
            .size(Size.ORIGINAL)
            .build()
    )

    if (painter.state is AsyncImagePainter.State.Success) {
        val size = painter.intrinsicSize

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(size.width / size.height)
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GalleryTopAppBar(
    title: String,
    onNavigationIconClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(192.dp)
            .background(
                Brush.verticalGradient(
                    0F to GalleryPalette.BarColor,
                    0.55F to GalleryPalette.BarColor,
                    1F to Color.Transparent,
                )
            )
    ) {
        TopAppBar(
            title = { Text(text = title, fontSize = 18.sp) },
            navigationIcon = {
                CuiToolbarNavigationIcon(
                    painter = painterResource(CommonDrawable.ic_arrow_back),
                    color = GalleryPalette.ContentColor,
                    onBackPress = onNavigationIconClick,
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                navigationIconContentColor = GalleryPalette.ContentColor,
                actionIconContentColor = GalleryPalette.ContentColor,
                titleContentColor = GalleryPalette.ContentColor,
            ),
        )
    }
}

@ScreenPreview
@Composable
private fun GalleryScreenPreview() = PreviewTheme {
    GalleryScreen(state = mockUiState)
}

internal val mockUiState = GalleryUiState(
    titleText = "3 of 21",
    isUiShown = true,
    picturesUri = persistentListOf("", ""),
    currentPicturePosition = 0,
)