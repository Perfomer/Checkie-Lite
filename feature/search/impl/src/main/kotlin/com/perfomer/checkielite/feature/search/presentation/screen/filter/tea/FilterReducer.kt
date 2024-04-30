package com.perfomer.checkielite.feature.search.presentation.screen.filter.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.requireContent
import com.perfomer.checkielite.common.pure.util.toArrayList
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.core.entity.search.RatingRange
import com.perfomer.checkielite.core.entity.search.SearchFilters
import com.perfomer.checkielite.feature.search.presentation.navigation.FilterResult
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterCommand
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterCommand.LoadTags
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterEffect
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterEvent
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterEvent.Initialize
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterEvent.TagsLoading
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterNavigationCommand.Exit
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterNavigationCommand.OpenTags
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterNavigationEvent
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterState
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterUiEvent.OnBackPress
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterUiEvent.OnMaxRatingSelected
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterUiEvent.OnMinRatingSelected
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterUiEvent.OnResetClick
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterUiEvent.OnTagClick
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterUiEvent.OnViewAllTagsClick

internal class FilterReducer : DslReducer<FilterCommand, FilterEffect, FilterEvent, FilterState>() {

    override fun reduce(event: FilterEvent) = when (event) {
        is Initialize -> reduceInitialize()
        is FilterUiEvent -> reduceUi(event)
        is TagsLoading -> reduceTagsLoading(event)
        is FilterNavigationEvent -> reduceNavigation(event)
    }

    private fun reduceInitialize() {
        commands(LoadTags)
    }

    private fun reduceUi(event: FilterUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
        is OnDoneClick -> commands(
            ExitWithResult(
                FilterResult.Applied(
                    SearchFilters(
                        tags = state.selectedTags.toArrayList(),
                        ratingRange = RatingRange(min = state.minRating, max = state.maxRating),
                    )
                )
            )
        )

        is OnMinRatingSelected -> state { copy(minRating = event.rating) }
        is OnMaxRatingSelected -> state { copy(maxRating = event.rating) }
        is OnResetClick -> commands(ExitWithResult(FilterResult.Reset))
        is OnTagClick -> {
            val allTags = state.suggestedTags.requireContent()
            val isAdding = state.selectedTags.none { it.id == event.tadId }
            val tag = allTags.first { it.id == event.tadId }

            if (isAdding) {
                state { copy(selectedTags = selectedTags + tag) }
            } else {
                state { copy(selectedTags = selectedTags - tag) }
            }
        }

        is OnViewAllTagsClick -> commands(OpenTags(state.selectedTags))
    }

    private fun reduceNavigation(event: FilterNavigationEvent) = when (event) {
        is FilterNavigationEvent.OnTagsSelected -> Unit
    }

    private fun reduceTagsLoading(event: TagsLoading) = when (event) {
        is TagsLoading.Started -> Unit
        is TagsLoading.Succeed -> state { copy(suggestedTags = Lce.Content(event.tags)) }
        is TagsLoading.Failed -> Unit
    }
}