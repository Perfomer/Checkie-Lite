package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea

import com.perfomer.checkielite.feature.gallery.presentation.selection.GallerySelectionChannel
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.actor.ObserveGallerySelectionActor
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand.ObserveGallerySelection
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.OpenGallery
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ReviewDetailsGallerySelectionTest {

    @Test
    fun `gallery selections update details and navigation does not interrupt observation`() = runBlocking {
        withTimeout(5_000L) {
            val selections = MutableSharedFlow<Int>()
            val channel = object : GallerySelectionChannel {
                override fun observe(): SharedFlow<Int> = selections
                override suspend fun select(position: Int) = selections.emit(position)
            }
            val events = async {
                ObserveGallerySelectionActor(channel)
                    .act(flowOf(ObserveGallerySelection, OpenGallery(listOf("first", "second", "third"), 0)))
                    .take(2)
                    .toList()
            }
            selections.subscriptionCount.first { it == 1 }
            channel.select(2)
            channel.select(1)

            var state = ReviewDetailsState(reviewId = "review")
            events.await().zip(listOf(2, 1)).forEach { (event, position) ->
                val update = ReviewDetailsReducer().reduce(state, event)
                state = requireNotNull(update.state)
                assertEquals(position, state.currentPicturePosition)
                assertTrue(update.commands.isEmpty())
            }
        }
    }
}
