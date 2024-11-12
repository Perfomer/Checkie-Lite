package com.perfomer.checkielite.core.navigation

fun navigation(block: NavigationRegistry.() -> Unit) = NavigationRegistry.apply(block)

inline fun <reified D : Destination, reified S : Screen> NavigationRegistry.associate() {
    associate(
        destinationClass = D::class,
        destinationSerializer = kotlinx.serialization.serializer<D>(),
        screenClass = S::class,
    )
}
