package com.perfomer.checkielite.common.tea.util

import com.perfomer.checkielite.common.pure.util.runIf
import com.perfomer.checkielite.common.pure.util.runSuspendCatching
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.exception.UnhandledExceptionHandler
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry

internal fun <Command : Any, Event : Any> combineActors(
    actors: Set<Actor<Command, Event>>,
    exceptionHandler: UnhandledExceptionHandler?,
): Actor<Command, Event> {
    return Actor { commands ->
        actors.mapNotNull { actor ->
            var latestCommand: Command? = null
            var failingCommand: Command? = null

            val safeCommandsFlow = commands
                .onEach { latestCommand = it }
                .dropWhile { it === failingCommand } // Remove failing command from Flow

            // Double check with `exceptionHandler` needed for Flow creation and Flow execution
            runSafeWith(exceptionHandler) { actor.act(safeCommandsFlow) }
                ?.runIf(exceptionHandler != null) {
                    retry {
                        failingCommand = latestCommand
                        exceptionHandler?.handle(it)
                        true
                    }
                }
        }
            .merge()
    }
}

internal inline fun <T> runSafeWith(exceptionHandler: UnhandledExceptionHandler?, block: () -> T): T? {
    if (exceptionHandler == null) {
        return block()
    }

    return runSuspendCatching(block)
        .onFailure(exceptionHandler::handle)
        .getOrNull()
}