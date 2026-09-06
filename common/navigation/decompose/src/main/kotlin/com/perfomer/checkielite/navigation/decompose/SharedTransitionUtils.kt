package com.perfomer.checkielite.navigation.decompose

import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.SharedTransitionDestination

internal fun Destination.hasSharedTransitionWith(other: Destination): Boolean =
    this is SharedTransitionDestination &&
        other is SharedTransitionDestination &&
        sharedTransitionGroup == other.sharedTransitionGroup
