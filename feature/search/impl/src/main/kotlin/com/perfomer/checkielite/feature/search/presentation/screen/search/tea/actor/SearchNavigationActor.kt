package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.entity.search.SearchFilters
import com.perfomer.checkielite.core.navigation.api.DestinationMode
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsParams
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsScreenProvider
import com.perfomer.checkielite.feature.search.presentation.navigation.FilterParams
import com.perfomer.checkielite.feature.search.presentation.navigation.FilterResult
import com.perfomer.checkielite.feature.search.presentation.navigation.FilterScreenProvider
import com.perfomer.checkielite.feature.search.presentation.navigation.SortParams
import com.perfomer.checkielite.feature.search.presentation.navigation.SortResult
import com.perfomer.checkielite.feature.search.presentation.navigation.SortScreenProvider
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.Exit
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenFilters
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenReviewDetails
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenSort
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent.OnFiltersUpdated
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent.OnSortUpdated
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class SearchNavigationActor(
    private val router: Router,
    private val reviewDetailsScreenProvider: ReviewDetailsScreenProvider,
    private val sortScreenProvider: SortScreenProvider,
    private val filterScreenProvider: FilterScreenProvider,
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
            is OpenSort -> return openSort(command)
            is OpenFilters -> return openFilters(command)
        }

        return null
    }

    private fun openReviewDetails(command: OpenReviewDetails) {
        val params = ReviewDetailsParams(command.reviewId)
        router.navigate(reviewDetailsScreenProvider(params))
    }

    private suspend fun openSort(command: OpenSort): SearchNavigationEvent {
        val result = router.navigateForResult<SortResult>(
            screen = sortScreenProvider(SortParams(command.currentSorting)),
            mode = DestinationMode.BOTTOM_SHEET,
        )

        return when (result) {
            is SortResult.Success -> OnSortUpdated(sorting = result.sorting)
        }
    }

    private suspend fun openFilters(command: OpenFilters): SearchNavigationEvent {
        val result = router.navigateForResult<FilterResult>(
            screen = filterScreenProvider(FilterParams(command.currentFilters)),
            mode = DestinationMode.BOTTOM_SHEET,
        )

        return when (result) {
            is FilterResult.Applied -> OnFiltersUpdated(result.filters)
            is FilterResult.Reset -> OnFiltersUpdated(SearchFilters())
        }
    }
}