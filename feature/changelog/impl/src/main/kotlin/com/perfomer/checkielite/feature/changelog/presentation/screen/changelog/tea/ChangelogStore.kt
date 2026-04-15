package com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ComponentStore
import com.perfomer.checkielite.common.ui.util.tea.LogUnhandledExceptionHandler
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogCommand
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogEffect
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogEvent
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogEvent.Initialize
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogState
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogUiEvent
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.ui.state.ChangelogUiState
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.ui.state.ChangelogUiStateMapper

internal class ChangelogStore(
    componentContext: ComponentContext,
    reducer: ChangelogReducer,
    uiStateMapper: ChangelogUiStateMapper,
    actors: Set<Actor<ChangelogCommand, ChangelogEvent>>,
) : ComponentStore<ChangelogCommand, ChangelogEffect, ChangelogEvent, ChangelogUiEvent, ChangelogState, ChangelogUiState>(
    componentContext = componentContext,
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialState = ChangelogState(),
    initialEvents = listOf(Initialize),
    unhandledExceptionHandler = LogUnhandledExceptionHandler("ChangelogStore"),
)
