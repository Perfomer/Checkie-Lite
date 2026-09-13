package com.perfomer.checkielite.feature.gallery.presentation.selection

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal class GallerySelectionChannelImpl : GallerySelectionChannel {

    private val selections: MutableSharedFlow<Int> = MutableSharedFlow()

    override fun observe(): SharedFlow<Int> = selections.asSharedFlow()

    override suspend fun select(position: Int) {
        selections.emit(position)
    }
}
