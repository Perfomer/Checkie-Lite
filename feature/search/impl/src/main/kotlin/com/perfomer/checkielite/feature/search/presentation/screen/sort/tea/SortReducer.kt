package com.perfomer.checkielite.feature.search.presentation.screen.sort.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.search.presentation.navigation.SortResult
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortCommand
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortEffect
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortEvent
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortNavigationCommand.Exit
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortState
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortUiEvent.OnBackPress
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortUiEvent.OnSortingOptionClick

internal class SortReducer : DslReducer<SortCommand, SortEffect, SortEvent, SortState>() {

    override fun reduce(event: SortEvent) = when (event) {
        is SortUiEvent -> reduceUi(event)
    }

    private fun reduceUi(event: SortUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
        is OnDoneClick -> commands(ExitWithResult(SortResult.Success(state.currentOption)))
        is OnSortingOptionClick -> state { copy(currentOption = event.type) }
    }
}