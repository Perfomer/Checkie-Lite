package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core

internal sealed interface GalleryEvent

internal sealed interface GalleryUiEvent : GalleryEvent {

    data object OnBackPress : GalleryUiEvent
}

internal sealed interface GalleryNavigationEvent : GalleryEvent