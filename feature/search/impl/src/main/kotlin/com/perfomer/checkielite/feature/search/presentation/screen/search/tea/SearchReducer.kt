package com.perfomer.checkielite.feature.search.presentation.screen.search.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEffect
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.Exit
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchState
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnBackPress

internal class SearchReducer : DslReducer<SearchCommand, SearchEffect, SearchEvent, SearchState>() {

    override fun reduce(event: SearchEvent) = when (event) {
        is SearchUiEvent -> reduceUi(event)
    }

    private fun reduceUi(event: SearchUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
    }
}