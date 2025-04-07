package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ComponentStore
import com.perfomer.checkielite.common.ui.util.tea.LogUnhandledExceptionHandler
import com.perfomer.checkielite.feature.gallery.navigation.GalleryDestination
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryCommand
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryEffect
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryEvent
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryState
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryUiEvent
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui.state.GalleryUiState
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui.state.GalleryUiStateMapper
import kotlinx.collections.immutable.toPersistentList

internal class GalleryStore(
    componentContext: ComponentContext,
    destination: GalleryDestination,
    reducer: GalleryReducer,
    uiStateMapper: GalleryUiStateMapper,
    actors: Set<Actor<GalleryCommand, GalleryEvent>>,
) : ComponentStore<GalleryCommand, GalleryEffect, GalleryEvent, GalleryUiEvent, GalleryState, GalleryUiState>(
    componentContext = componentContext,
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialState = GalleryState(
        picturesUri = destination.picturesUri.toPersistentList(),
        currentPicturePosition = destination.currentPicturePosition,
    ),
    unhandledExceptionHandler = LogUnhandledExceptionHandler("GalleryStore"),
)