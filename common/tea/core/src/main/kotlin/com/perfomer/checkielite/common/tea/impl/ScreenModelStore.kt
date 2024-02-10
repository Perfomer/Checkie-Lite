package com.perfomer.checkielite.common.tea.impl

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.perfomer.checkielite.common.tea.Store
import com.perfomer.checkielite.common.tea.TeaEngine
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.component.Reducer
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.common.tea.util.combineActors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus

/**
 * [Store] based on Voyager's [ScreenModel]
 *
 * @see TeaEngine
 */
abstract class ScreenModelStore<Command : Any, Effect : Any, Event : Any, UiEvent : Event, State : Any, UiState : Any> internal constructor(
    engine: TeaEngine<Command, Effect, Event, UiEvent, State, UiState>,
) : ScreenModel, Store<Effect, UiEvent, UiState> by engine {

	constructor(
		initialState: State,
		initialEvents: List<Event> = emptyList(),
		reducer: Reducer<Command, Effect, Event, State>,
		uiStateMapper: UiStateMapper<State, UiState>? = null,
		actors: Set<Actor<Command, Event>>,
	) : this(engine = TeaEngine(initialState, initialEvents, reducer, uiStateMapper, combineActors(actors)))

	init {
		engine.launch(screenModelScope + Dispatchers.Main.immediate)
	}
}