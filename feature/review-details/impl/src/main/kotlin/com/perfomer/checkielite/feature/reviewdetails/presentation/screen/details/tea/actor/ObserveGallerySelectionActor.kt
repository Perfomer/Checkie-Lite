package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.feature.gallery.presentation.selection.GallerySelectionChannel
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand.ObserveGallerySelection
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent.GalleryPictureSelected
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class ObserveGallerySelectionActor(
    private val selectionChannel: GallerySelectionChannel,
) : Actor<ReviewDetailsCommand, ReviewDetailsEvent> {

    override fun act(commands: Flow<ReviewDetailsCommand>): Flow<ReviewDetailsEvent> {
        return commands.filterIsInstance<ObserveGallerySelection>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: ObserveGallerySelection): Flow<ReviewDetailsEvent> {
        return selectionChannel.observe()
            .map(::GalleryPictureSelected)
    }
}
