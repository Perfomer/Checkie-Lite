package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.gallery.navigation.GalleryParams
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.GalleryStore
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryUiEvent.OnBackPress
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryUiEvent.OnPagerClick
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryUiEvent.OnPictureSelect
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class GalleryContentScreen(
    private val params: GalleryParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<GalleryStore>(params)) { state ->
        BackHandlerWithLifecycle { accept(OnBackPress) }

        GalleryScreen(
            state = state,
            onNavigationIconClick = acceptable(OnBackPress),
            onDismiss = acceptable(OnBackPress),
            onPageChange = acceptable(::OnPictureSelect),
            onPagerClick = acceptable(OnPagerClick),
        )
    }
}