package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui.state

import android.content.Context
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.gallery.R
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryState

internal class GalleryUiStateMapper(
    private val context: Context,
) : UiStateMapper<GalleryState, GalleryUiState> {

    override fun map(state: GalleryState): GalleryUiState {
        return GalleryUiState(
            titleText = context.getString(R.string.gallery_title, state.currentPicturePosition + 1, state.picturesUri.size),
            picturesUri = state.picturesUri,
            currentPicturePosition = state.currentPicturePosition,
        )
    }
}