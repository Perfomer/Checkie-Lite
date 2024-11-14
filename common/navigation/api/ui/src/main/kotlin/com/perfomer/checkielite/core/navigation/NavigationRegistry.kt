package com.perfomer.checkielite.core.navigation

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SealedClassSerializer
import kotlin.reflect.KClass

object NavigationRegistry {

    private val registry: MutableMap<KClass<out Destination>, KClass<out Screen>> = mutableMapOf()
    private val serializers: MutableList<KSerializer<out Destination>> = mutableListOf()

    @OptIn(InternalSerializationApi::class)
    fun serializer(): KSerializer<Destination> {
        return SealedClassSerializer(
            serialName = "Destination",
            baseClass = Destination::class,
            subclasses = registry.keys.toTypedArray(),
            subclassSerializers = serializers.toTypedArray(),
        )
    }

    fun obtain(destinationClass: KClass<out Destination>): KClass<out Screen> {
        return requireNotNull(registry[destinationClass]) {
            "Destination `${destinationClass.simpleName}` is not registered!"
        }
    }

    fun <T : Destination> register(
        destinationClass: KClass<T>,
        destinationSerializer: KSerializer<T>,
        screenClass: KClass<out Screen>,
    ) {
        registry[destinationClass] = screenClass
        serializers += destinationSerializer
    }
}
