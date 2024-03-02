@file:OptIn(ExperimentalFoundationApi::class)

package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.cui.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.PreviewTheme
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui.state.GalleryUiState
import com.perfomer.checkielite.feature.gallery.presentation.util.resetZoomOnMoveOut
import kotlinx.collections.immutable.persistentListOf
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
internal fun GalleryScreen(
    state: GalleryUiState,
    onNavigationIconClick: () -> Unit = {},
    onPageChange: (pageIndex: Int) -> Unit = {},
) {
    Scaffold(
        topBar = {
            GalleryTopAppBar(
                title = state.titleText,
                onNavigationIconClick = onNavigationIconClick,
            )
        },
    ) {
        Content(
            state = state,
            onPageChange = onPageChange,
        )
    }
}

@Composable
private fun Content(
    state: GalleryUiState,
    onPageChange: (pageIndex: Int) -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = { state.picturesUri.size },
        initialPage = state.currentPicturePosition,
    )

    UpdateEffect(pagerState.currentPage) { onPageChange(pagerState.currentPage) }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(GalleryPalette.BackgroundColor),
    ) { page ->
        val zoomState = rememberZoomState()

        AsyncImage(
            model = state.picturesUri[page],
            contentDescription = null,
            onSuccess = { pictureState -> zoomState.setContentSize(pictureState.painter.intrinsicSize) },
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .zoomable(zoomState)
                .resetZoomOnMoveOut(page, zoomState, pagerState)
        )
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
    picturesUri = persistentListOf("", ""),
    currentPicturePosition = 0,
)