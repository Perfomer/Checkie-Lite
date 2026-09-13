package com.perfomer.checkielite.core.navigation

import com.perfomer.checkielite.core.navigation.transition.SharedContentGroup
import com.perfomer.checkielite.core.navigation.transition.SharedContentMatch
import com.perfomer.checkielite.core.navigation.transition.SharedContentRole
import com.perfomer.checkielite.core.navigation.transition.SharedTransitionPolicy
import kotlinx.serialization.serializer

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
annotation class NavigationDsl

@NavigationDsl
class SharedTransitionDsl {
    private val matches = mutableSetOf<SharedContentMatch>()

    fun match(source: SharedContentRole, target: SharedContentRole) {
        matches += SharedContentMatch(source, target)
    }

    fun build(): SharedTransitionPolicy = SharedTransitionPolicy(matches)
}

@NavigationDsl
inline fun <reified Source : Destination, reified Target : Destination> NavigationRegistry.sharedTransition(
    block: SharedTransitionDsl.() -> Unit,
) {
    registerSharedTransition(Source::class, Target::class, SharedTransitionDsl().apply(block).build())
}

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
inline fun <reified Source : Destination, reified Target : Destination> NavigationRegistry.sharedTransition(
    vararg groups: SharedContentGroup,
) {
    registerSharedTransition(
        source = Source::class,
        target = Target::class,
        groups = groups
            .toSet()
            .ifEmpty { setOf(SharedContentGroup.Default) },
    )
}
