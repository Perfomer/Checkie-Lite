package com.perfomer.checkielite.core.navigation

/**
 * Optional content supplied only when a navigation entry is created; never saved with its route.
 *
 * [D] is invariant so content for one destination cannot be passed to another destination.
 * Screens must also support creation without initial content after state restoration.
 */
interface InitialContent<D : Destination>
