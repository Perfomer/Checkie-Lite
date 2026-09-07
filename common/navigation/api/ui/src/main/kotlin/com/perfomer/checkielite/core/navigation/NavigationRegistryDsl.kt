package com.perfomer.checkielite.core.navigation

import kotlinx.serialization.serializer

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
annotation class NavigationDsl

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

/** Registers a directional shared transition. The same edge is reused when navigating back. */
@NavigationDsl
inline fun <reified Source : Destination, reified Target : Destination> NavigationRegistry.sharedTransition() {
    registerSharedTransition(
        source = Source::class,
        target = Target::class,
    )
}
