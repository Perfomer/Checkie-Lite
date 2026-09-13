package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core

internal sealed interface GalleryCommand {

    class NotifyPictureSelected(val position: Int) : GalleryCommand
}

internal sealed interface GalleryNavigationCommand : GalleryCommand {

    data object Exit : GalleryNavigationCommand
}