package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.GalleryStore
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryUiEvent.OnBackPress
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryUiEvent.OnPagerClick
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryUiEvent.OnPictureSelect

internal class GalleryContentScreen(
    private val store: GalleryStore,
) : Screen {

    @Composable
    override fun Screen() = TeaComposable(store) { state ->
        BackHandler { accept(OnBackPress) }

        GalleryScreen(
            state = state,
            onNavigationIconClick = acceptable(OnBackPress),
            onDismiss = acceptable(OnBackPress),
            onPageChange = acceptable(::OnPictureSelect),
            onPagerClick = acceptable(OnPagerClick),
        )
    }
}