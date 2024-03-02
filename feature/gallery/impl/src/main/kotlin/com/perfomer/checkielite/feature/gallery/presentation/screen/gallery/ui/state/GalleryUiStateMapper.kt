package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui.state

import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryState

internal class GalleryUiStateMapper : UiStateMapper<GalleryState, GalleryUiState> {

    override fun map(state: GalleryState): GalleryUiState {
        return GalleryUiState(
            picturesUri = state.picturesUri,
            currentPicturePosition = state.currentPicturePosition,
        )
    }
}