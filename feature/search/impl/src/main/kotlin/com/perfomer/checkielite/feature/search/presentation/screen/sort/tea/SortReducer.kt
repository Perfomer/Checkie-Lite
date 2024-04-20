package com.perfomer.checkielite.feature.search.presentation.screen.sort.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortCommand
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortEffect
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortEvent
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortNavigationCommand.Exit
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortState
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortUiEvent.OnBackPress

internal class SortReducer : DslReducer<SortCommand, SortEffect, SortEvent, SortState>() {

    override fun reduce(event: SortEvent) = when (event) {
        is SortUiEvent -> reduceUi(event)
    }

    private fun reduceUi(event: SortUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
    }
}