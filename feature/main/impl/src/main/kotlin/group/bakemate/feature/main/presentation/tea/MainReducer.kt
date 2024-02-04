package group.bakemate.feature.main.presentation.tea

import group.bakemate.feature.main.presentation.tea.core.MainCommand
import group.bakemate.feature.main.presentation.tea.core.MainEffect
import group.bakemate.feature.main.presentation.tea.core.MainEvent
import group.bakemate.feature.main.presentation.tea.core.MainEvent.*
import group.bakemate.feature.main.presentation.tea.core.MainState
import group.bakemate.feature.main.presentation.tea.core.MainUiEvent
import group.bakemate.feature.main.presentation.tea.core.MainUiEvent.*
import group.bakemate.tea.tea.dsl.DslReducer

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