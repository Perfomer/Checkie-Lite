package com.perfomer.checkielite.navigation.decompose

import com.arkivanov.decompose.extensions.compose.stack.animation.Direction
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.NavigationRegistry

internal fun Destination.hasSharedTransitionWith(
    other: Destination,
    direction: Direction,
): Boolean {
    val (source, target) = when (direction) {
        Direction.ENTER_FRONT,
        Direction.EXIT_FRONT -> other to this
        Direction.ENTER_BACK,
        Direction.EXIT_BACK -> this to other
    }
    return NavigationRegistry.hasSharedTransition(source = source, target = target)
}
