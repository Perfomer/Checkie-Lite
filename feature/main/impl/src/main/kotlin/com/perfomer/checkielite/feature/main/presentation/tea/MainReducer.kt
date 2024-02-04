package com.perfomer.checkielite.feature.main.presentation.tea

import com.perfomer.checkielite.feature.main.presentation.tea.core.MainCommand
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainEffect
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainEvent
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainEvent.*
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainState
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainUiEvent
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainUiEvent.*
import com.perfomer.checkielite.tea.tea.dsl.DslReducer

internal class MainReducer : DslReducer<MainCommand, MainEffect, MainEvent, MainState>() {

    override fun reduce(event: MainEvent) = when (event) {
        is MainUiEvent -> reduceUi(event)
        is Initialize -> Unit
    }

    private fun reduceUi(event: MainUiEvent) = when (event) {
        is OnFabClick -> Unit
        is OnReviewClick -> Unit
        is OnSearchQueryInput -> Unit
    }
}