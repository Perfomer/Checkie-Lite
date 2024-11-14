package com.perfomer.checkielite.core.navigation

import kotlinx.serialization.serializer

fun navigation(block: NavigationRegistry.() -> Unit) = NavigationRegistry.apply(block)

inline fun <reified D : Destination, reified S : Screen> NavigationRegistry.associate() {
    register(
        destinationClass = D::class,
        destinationSerializer = serializer<D>(),
        screenClass = S::class,
    )
}
