@file:OptIn(ExperimentalFoundationApi::class)

package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.PreviewTheme
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui.state.GalleryUiState
import com.perfomer.checkielite.feature.gallery.presentation.util.resetZoomOnMoveOut
import kotlinx.collections.immutable.persistentListOf
import moe.tlaster.swiper.Swiper
import moe.tlaster.swiper.rememberSwiperState
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

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
    val swiperState = rememberSwiperState(onDismiss = onDismiss)

    UpdateEffect(swiperState.progress) {
        onDismissProgressChange(swiperState.progress)
    }

    val pagerState = rememberPagerState(
        pageCount = { state.picturesUri.size },
        initialPage = state.currentPicturePosition,
    )

    UpdateEffect(pagerState.currentPage) { onPageChange(pagerState.currentPage) }

    Swiper(state = swiperState) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val zoomState = rememberZoomState()

            AsyncImage(
                model = state.picturesUri[page],
                contentDescription = null,
                onSuccess = { pictureState -> zoomState.setContentSize(pictureState.painter.intrinsicSize) },
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .zoomable(
                        zoomState = zoomState,
                        onTap = { onPagerClick() },
                    )
                    .resetZoomOnMoveOut(page, zoomState, pagerState)
            )
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