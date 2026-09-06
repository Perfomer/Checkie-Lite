package com.perfomer.checkielite.core.navigation

/** Destinations in the same group can animate shared content without moving the whole screen. */
interface SharedTransitionDestination {

    val sharedTransitionGroup: String
}
