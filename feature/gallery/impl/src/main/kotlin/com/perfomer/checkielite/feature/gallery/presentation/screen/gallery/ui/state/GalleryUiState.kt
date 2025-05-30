package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui.state

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class GalleryUiState(
    val titleText: String,
    val isUiShown: Boolean,
    val picturesUri: ImmutableList<String>,
    val currentPicturePosition: Int,
)