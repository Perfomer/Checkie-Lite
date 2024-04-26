package com.perfomer.checkielite.feature.search.presentation.screen.filter.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterCommand
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterEffect
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterEvent
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterNavigationCommand.Exit
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterState
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterUiEvent.OnBackPress
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterUiEvent.OnDoneClick

internal class FilterReducer : DslReducer<FilterCommand, FilterEffect, FilterEvent, FilterState>() {

    override fun reduce(event: FilterEvent) = when (event) {
        is FilterUiEvent -> reduceUi(event)
    }

    private fun reduceUi(event: FilterUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
        is OnDoneClick -> Unit
    }
}