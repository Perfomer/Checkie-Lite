package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core

import kotlinx.collections.immutable.PersistentList

internal data class GalleryState(
    val picturesUri: PersistentList<String>,
    val currentPicturePosition: Int = 0,
    val isUiShown: Boolean = true,
)