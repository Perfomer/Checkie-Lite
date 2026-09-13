package com.perfomer.checkielite.feature.gallery.presentation.selection

import kotlinx.coroutines.flow.SharedFlow

/** Publishes selection changes from the currently open gallery without replaying old values. */
interface GallerySelectionChannel {

    fun observe(): SharedFlow<Int>

    suspend fun select(position: Int)
}
