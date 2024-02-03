package group.bakemate.tea.tea.util

import group.bakemate.tea.tea.component.Actor
import kotlinx.coroutines.flow.merge

internal fun <Command : Any, Event : Any> combineActors(
    actors: Set<Actor<Command, Event>>,
): Actor<Command, Event> {
	return Actor { commands ->
		actors
			.map { actor -> actor.act(commands) }
			.merge()
	}
}