package com.perfomer.checkielite.newnavigation

import kotlin.reflect.KClass

object NavigationRegistry {

    private val registry: MutableMap<KClass<out Destination>, KClass<out BaseDecomposeScreen>> = mutableMapOf()

    fun obtain(destinationClass: KClass<out Destination>): KClass<out BaseDecomposeScreen> {
        return requireNotNull(registry[destinationClass]) { "Destination `${destinationClass.simpleName}` is not registered!" }
    }

    fun associate(destinationClass: KClass<out Destination>, screenClass: KClass<out BaseDecomposeScreen>) {
        registry[destinationClass] = screenClass
    }

    operator fun invoke(block: NavigationRegistry.() -> Unit) = apply(block)
}

inline fun <reified D : Destination, reified S : BaseDecomposeScreen> NavigationRegistry.associate() {
    associate(destinationClass = D::class, screenClass = S::class)
}