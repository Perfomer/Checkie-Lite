package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.actor

import com.perfomer.checkielite.common.pure.util.ignoreResult
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryCommand
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryCommand.NotifyPictureSelected
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryEvent
import com.perfomer.checkielite.feature.gallery.presentation.selection.GallerySelectionChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

internal class NotifyPictureSelectedActor(
    private val selectionChannel: GallerySelectionChannel,
) : Actor<GalleryCommand, GalleryEvent> {

    override fun act(commands: Flow<GalleryCommand>): Flow<GalleryEvent> {
        return commands.filterIsInstance<NotifyPictureSelected>()
            .mapLatest(::handleCommand)
            .ignoreResult()
    }

    private suspend fun handleCommand(command: NotifyPictureSelected) {
        selectionChannel.select(command.position)
    }
}
