package com.perfomer.checkielite.common.tea.dsl

import com.perfomer.checkielite.common.tea.component.Update

internal class Updater<Command : Any, Effect : Any, State : Any>(
    currentState: State,
) {

    var state: State = currentState
        private set

    private val commands: MutableList<Command> = mutableListOf()
    private val effects: MutableList<Effect> = mutableListOf()

    fun state(block: State.() -> State) {
        state = block.invoke(state)
    }

    fun commands(vararg commands: Command?) {
        commands(commands.toList())
    }

    fun commands(commands: Iterable<Command?>) {
        this.commands += commands.filterNotNull()
    }

    fun effects(vararg effects: Effect?) {
        effects(effects.toList())
    }

    fun effects(effects: Iterable<Effect?>) {
        this.effects += effects.filterNotNull()
    }

    internal fun collect(): Update<Command, Effect, State> {
        return Update(
            state = state,
            commands = commands,
            effects = effects,
        )
    }
}