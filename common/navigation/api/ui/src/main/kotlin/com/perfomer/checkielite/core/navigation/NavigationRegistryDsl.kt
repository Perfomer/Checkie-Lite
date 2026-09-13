package com.perfomer.checkielite.core.navigation

import com.perfomer.checkielite.core.navigation.transition.SharedContentGroup
import com.perfomer.checkielite.core.navigation.transition.SharedContentMatch
import com.perfomer.checkielite.core.navigation.transition.SharedContentRole
import com.perfomer.checkielite.core.navigation.transition.SharedTransitionPolicy
import kotlin.reflect.KClass
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

/** All transitions in this scope originate from the associated destination. */
@NavigationDsl
class DestinationNavigationDsl(
    @PublishedApi internal val registry: NavigationRegistry,
    @PublishedApi internal val source: KClass<out Destination>,
) {
    inline fun <reified Target : Destination> sharedTransitionWith(block: SharedTransitionDsl.() -> Unit) {
        registry.registerSharedTransition(source, Target::class, SharedTransitionDsl().apply(block).build())
    }

    inline fun <reified Target : Destination> sharedTransitionWith(vararg groups: SharedContentGroup) {
        registry.registerSharedTransition(
            source = source,
            target = Target::class,
            groups = groups.toSet().ifEmpty { setOf(SharedContentGroup.Default) },
        )
    }
}

@NavigationDsl
fun navigation(block: NavigationRegistry.() -> Unit) = NavigationRegistry.apply(block)

@NavigationDsl
inline fun <reified D : Destination, reified S : Screen> NavigationRegistry.associate(
    block: DestinationNavigationDsl.() -> Unit = {},
) {
    register(
        destinationClass = D::class,
        destinationSerializer = serializer<D>(),
        screenClass = S::class,
    )
    DestinationNavigationDsl(this, D::class).block()
}
