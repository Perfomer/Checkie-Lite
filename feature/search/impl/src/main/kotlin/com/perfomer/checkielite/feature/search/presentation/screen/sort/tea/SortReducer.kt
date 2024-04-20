package com.perfomer.checkielite.feature.search.presentation.screen.sort.tea

import com.perfomer.checkielite.common.pure.util.next
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.core.data.datasource.sort.SortingOrder
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
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortUiEvent.OnSortingOrderClick

internal class SortReducer : DslReducer<SortCommand, SortEffect, SortEvent, SortState>() {

    override fun reduce(event: SortEvent) = when (event) {
        is SortUiEvent -> reduceUi(event)
    }

    private fun reduceUi(event: SortUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
        is OnDoneClick -> commands(ExitWithResult(SortResult.Success(order = state.currentOrder, strategy = state.currentOption)))
        is OnSortingOrderClick -> state { copy(currentOrder = currentOrder.next() ?: SortingOrder.entries.first()) }
        is OnSortingOptionClick -> state { copy(currentOption = event.type) }
    }
}