package com.perfomer.checkielite.core.navigation

import kotlin.reflect.KClass

object NavigationRegistry {

    private val registry: MutableMap<KClass<out Destination>, KClass<out Screen>> = mutableMapOf()

    fun obtain(destinationClass: KClass<out Destination>): KClass<out Screen> {
        return requireNotNull(registry[destinationClass]) { "Destination `${destinationClass.simpleName}` is not registered!" }
    }

    fun associate(destinationClass: KClass<out Destination>, screenClass: KClass<out Screen>) {
        registry[destinationClass] = screenClass
    }
}

fun navigation(block: NavigationRegistry.() -> Unit) = NavigationRegistry.apply(block)

inline fun <reified D : Destination, reified S : Screen> NavigationRegistry.associate() {
    associate(destinationClass = D::class, screenClass = S::class)
}