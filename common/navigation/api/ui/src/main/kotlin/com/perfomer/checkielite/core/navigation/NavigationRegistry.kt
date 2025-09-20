package com.perfomer.checkielite.core.navigation

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.reflect.KClass

object NavigationRegistry {

    private val registry: MutableMap<KClass<out Destination>, KClass<out Screen>> = mutableMapOf()
    private val serializers: MutableList<KSerializer<out Destination>> = mutableListOf()

    @OptIn(InternalSerializationApi::class)
    fun serializer(): KSerializer<Destination> {
        return DestinationSerializer()
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

    private class DestinationSerializer : KSerializer<Destination> {

        private val baseSerializer = PolymorphicSerializer(Destination::class)
        override val descriptor: SerialDescriptor = baseSerializer.descriptor

        override fun serialize(encoder: Encoder, value: Destination) {
            val valueClass = value::class
            val serializer = registry.keys.zip(serializers)
                .find { (kClass, _) -> kClass == valueClass }
                ?.second
                ?: throw SerializationException("No serializer found for ${valueClass.simpleName}")

            encoder.encodeSerializableValue(serializer as KSerializer<Destination>, value)
        }

        override fun deserialize(decoder: Decoder): Destination {
            // For deserialization, we delegate to the polymorphic serializer
            return baseSerializer.deserialize(decoder)
        }
    }
}
