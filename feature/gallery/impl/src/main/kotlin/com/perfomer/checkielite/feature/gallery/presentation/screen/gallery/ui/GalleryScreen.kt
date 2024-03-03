@file:OptIn(ExperimentalFoundationApi::class)

package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
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
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.PreviewTheme
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui.state.GalleryUiState
import kotlinx.collections.immutable.persistentListOf

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

    UpdateEffect(state.isUiShown) {
        systemUiController.isSystemBarsVisible = state.isUiShown
    }

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
        MainHorizontalPager(
            state = state,
            onPageChange = onPageChange,
            onPagerClick = onPagerClick,
            onDismiss = onDismiss,
            onDismissProgressChange = { progress -> backgroundAlpha = 1 - progress }
        )
    }
}

@Composable
private fun MainHorizontalPager(
    state: GalleryUiState,
    onPageChange: (pageIndex: Int) -> Unit,
    onPagerClick: () -> Unit,
    onDismissProgressChange: (progress: Float) -> Unit,
    onDismiss: () -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = { state.picturesUri.size },
        initialPage = state.currentPicturePosition,
    )

    UpdateEffect(pagerState.currentPage) { onPageChange(pagerState.currentPage) }
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val zoomableState = rememberZoomableState(
            minScale = 0.8F,
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
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(state.picturesUri[page])
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GalleryTopAppBar(
    title: String,
    onNavigationIconClick: () -> Unit,
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
            containerColor = GalleryPalette.BarColor,
            navigationIconContentColor = GalleryPalette.ContentColor,
            actionIconContentColor = GalleryPalette.ContentColor,
            titleContentColor = GalleryPalette.ContentColor,
        ),
    )
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