package com.perfomer.checkielite.core.navigation

import kotlinx.serialization.serializer

@DslMarker
private annotation class NavigationDsl

@NavigationDsl
fun navigation(block: NavigationRegistry.() -> Unit) = NavigationRegistry.apply(block)

@NavigationDsl
inline fun <reified D : Destination, reified S : Screen> NavigationRegistry.associate() {
    register(
        destinationClass = D::class,
        destinationSerializer = serializer<D>(),
        screenClass = S::class,
    )
}
