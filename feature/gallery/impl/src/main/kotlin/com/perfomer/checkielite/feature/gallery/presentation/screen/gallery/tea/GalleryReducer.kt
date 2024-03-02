package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryCommand
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryEffect
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryEvent
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryNavigationCommand.Exit
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryNavigationEvent
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryState
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryUiEvent
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryUiEvent.OnBackPress

internal class GalleryReducer : DslReducer<GalleryCommand, GalleryEffect, GalleryEvent, GalleryState>() {

    override fun reduce(event: GalleryEvent) = when (event) {
        is GalleryUiEvent -> reduceUi(event)
        is GalleryNavigationEvent -> Unit
    }

    private fun reduceUi(event: GalleryUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
    }
}