package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ScreenModelStore
import com.perfomer.checkielite.feature.gallery.navigation.GalleryParams
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryCommand
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryEffect
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryEvent
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryState
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryUiEvent
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui.state.GalleryUiState
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui.state.GalleryUiStateMapper
import kotlinx.collections.immutable.toPersistentList

internal class GalleryStore(
    params: GalleryParams,
    reducer: GalleryReducer,
    uiStateMapper: GalleryUiStateMapper,
    actors: Set<Actor<GalleryCommand, GalleryEvent>>,
) : ScreenModelStore<GalleryCommand, GalleryEffect, GalleryEvent, GalleryUiEvent, GalleryState, GalleryUiState>(
    initialState = GalleryState(
        picturesUri = params.picturesUri.toPersistentList(),
        currentPicturePosition = params.currentPicturePosition,
    ),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
)