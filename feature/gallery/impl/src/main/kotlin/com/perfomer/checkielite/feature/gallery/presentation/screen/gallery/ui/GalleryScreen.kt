package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.common.ui.theme.PreviewTheme
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui.state.GalleryUiState
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun GalleryScreen(
    state: GalleryUiState,
    onNavigationIconClick: () -> Unit = {},
) {
    Scaffold(
        topBar = { TopAppBar() },
    ) { contentPadding ->
        Content(state = state, contentPadding = contentPadding)
    }
}

@Composable
private fun Content(
    state: GalleryUiState,
    contentPadding: PaddingValues,
) {

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text("")
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(CuiPalette.Light.BackgroundPrimary)
            .statusBarsPadding()
    )
}

@ScreenPreview
@Composable
private fun GalleryScreenPreview() = PreviewTheme {
    GalleryScreen(state = mockUiState)
}

internal val mockUiState = GalleryUiState(
    picturesUri = persistentListOf("", ""),
    currentPicturePosition = 0,
)