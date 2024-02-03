package group.bakemate.tea.tea.impl

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import group.bakemate.tea.tea.Store
import group.bakemate.tea.tea.TeaEngine
import group.bakemate.tea.tea.component.Actor
import group.bakemate.tea.tea.component.Reducer
import group.bakemate.tea.tea.component.UiStateMapper
import group.bakemate.tea.tea.util.combineActors
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
		engine.launch(coroutineScope + Dispatchers.Main.immediate)
	}
}