package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.DestinationMode
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsDestination
import com.perfomer.checkielite.feature.search.presentation.navigation.SortDestination
import com.perfomer.checkielite.feature.search.presentation.navigation.SortResult
import com.perfomer.checkielite.feature.search.presentation.navigation.TagsDestination
import com.perfomer.checkielite.feature.search.presentation.navigation.TagsResult
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.Exit
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenAllFilters
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenReviewDetails
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenSort
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenTags
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent.OnSortUpdated
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent.OnTagsUpdated
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class SearchNavigationActor(
    private val router: Router,
) : Actor<SearchCommand, SearchEvent> {

    override fun act(commands: Flow<SearchCommand>): Flow<SearchEvent> {
        return commands.filterIsInstance<SearchNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private suspend fun handleCommand(command: SearchNavigationCommand): SearchNavigationEvent? {
        when (command) {
            is Exit -> router.exit()
            is OpenReviewDetails -> openReviewDetails(command)
            is OpenAllFilters -> return openAllFilters(command)
            is OpenSort -> return openSort(command)
            is OpenTags -> return openTags(command)
        }

        return null
    }

    private fun openReviewDetails(command: OpenReviewDetails) {
        router.navigate(ReviewDetailsDestination(command.reviewId))
    }

    private suspend fun openAllFilters(command: OpenAllFilters): SearchNavigationEvent {
        TODO()
    }

    private suspend fun openSort(command: OpenSort): SearchNavigationEvent? {
        val result = router.navigateForResult<SortResult>(
            destination = SortDestination(command.currentSorting),
            mode = DestinationMode.BOTTOM_SHEET,
        )

        return when (result) {
            is SortResult.Success -> OnSortUpdated(sorting = result.sorting)
            null -> null
        }
    }

    private suspend fun openTags(command: OpenTags): SearchNavigationEvent? {
        val result = router.navigateForResult<TagsResult>(
            destination = TagsDestination(command.selectedTagsIds),
            mode = DestinationMode.BOTTOM_SHEET,
        )

        return when (result) {
            is TagsResult.Success -> OnTagsUpdated(result.selectedTagsIds)
            null -> null
        }
    }
}