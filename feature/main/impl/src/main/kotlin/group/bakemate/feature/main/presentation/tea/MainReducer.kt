package group.bakemate.feature.main.presentation.tea

import group.bakemate.feature.main.presentation.tea.core.MainCommand
import group.bakemate.feature.main.presentation.tea.core.MainEffect
import group.bakemate.feature.main.presentation.tea.core.MainEvent
import group.bakemate.feature.main.presentation.tea.core.MainState
import group.bakemate.feature.main.presentation.tea.core.MainUiEvent.Initialize
import group.bakemate.feature.main.presentation.tea.core.MainUiEvent.OnClick
import group.bakemate.tea.tea.dsl.DslReducer

internal class MainReducer : DslReducer<MainCommand, MainEffect, MainEvent, MainState>() {

    override fun reduce(event: MainEvent) = when (event) {

        is Initialize -> state { copy(currentContentType = state.currentContentType) }

        is OnClick -> state { copy(currentContentType = event.type) }
    }
}