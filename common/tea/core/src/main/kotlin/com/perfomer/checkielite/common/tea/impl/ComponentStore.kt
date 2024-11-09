package com.perfomer.checkielite.common.tea.impl

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.Store
import com.perfomer.checkielite.common.tea.TeaEngine
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.component.Reducer
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.common.tea.util.combineActors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * [Store] based on Decompose's [ComponentContext]
 *
 * @see TeaEngine
 */
abstract class ComponentStore<Command : Any, Effect : Any, Event : Any, UiEvent : Event, State : Any, UiState : Any> internal constructor(
    componentContext: ComponentContext,
    engine: TeaEngine<Command, Effect, Event, UiEvent, State, UiState>,
) : ComponentContext by componentContext,
    Store<Effect, UiEvent, UiState> by engine {

    constructor(
        componentContext: ComponentContext,
        initialState: State,
        initialEvents: List<Event> = emptyList(),
        reducer: Reducer<Command, Effect, Event, State>,
        uiStateMapper: UiStateMapper<State, UiState>? = null,
        actors: Set<Actor<Command, Event>>,
    ) : this(
        componentContext = componentContext,
        engine = TeaEngine(initialState, initialEvents, reducer, uiStateMapper, combineActors(actors)),
    )

    init {
        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        engine.launch(coroutineScope)
    }
}