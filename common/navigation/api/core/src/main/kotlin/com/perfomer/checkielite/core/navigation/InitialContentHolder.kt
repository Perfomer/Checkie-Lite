package com.perfomer.checkielite.core.navigation

/**
 * Always-present creation parameter supplied by the navigation host.
 * [value] is null when no content was supplied, including after state restoration.
 */
class InitialContentHolder<out C : InitialContent<*>>(
    val value: C?,
)
