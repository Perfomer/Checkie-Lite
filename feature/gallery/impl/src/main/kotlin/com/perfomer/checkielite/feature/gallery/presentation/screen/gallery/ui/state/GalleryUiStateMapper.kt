package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui.state

import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.feature.gallery.R
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryState

internal class GalleryUiStateMapper : UiStateMapper<GalleryState, GalleryUiState> {

    override fun map(state: GalleryState): GalleryUiState {
        return GalleryUiState(
            titleText = Text.resource(
                R.string.gallery_title,
                Text.raw(state.currentPicturePosition + 1),
                Text.raw(state.picturesUri.size),
            ),
            picturesUri = state.picturesUri,
            currentPicturePosition = state.currentPicturePosition,
            isUiShown = state.isUiShown,
        )
    }
}