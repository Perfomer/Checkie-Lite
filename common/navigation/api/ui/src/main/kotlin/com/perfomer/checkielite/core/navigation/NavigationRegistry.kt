package com.perfomer.checkielite.core.navigation

import com.perfomer.checkielite.core.navigation.transition.SharedContentGroup
import kotlin.reflect.KClass
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException

@NavigationDsl
object NavigationRegistry {

    private const val TYPE_FIELD = "type"
    private const val PAYLOAD_FIELD = "payload"

    private val registry: MutableMap<KClass<out Destination>, ScreenEntry> = mutableMapOf()
    private val sharedTransitions: MutableMap<SharedTransitionEdge, Set<SharedContentGroup>> = mutableMapOf()

    @OptIn(InternalSerializationApi::class)
    fun serializer(): KSerializer<Destination> {
        return DestinationSerializer()
    }

    fun obtain(destinationClass: KClass<out Destination>): KClass<out Screen> {
        return requireRegistration(destinationClass).screenClass
    }

    fun <T : Destination> register(
        destinationClass: KClass<T>,
        destinationSerializer: KSerializer<T>,
        screenClass: KClass<out Screen>,
    ) {
        registry[destinationClass] = ScreenEntry(
            screenClass = screenClass,
            serializer = destinationSerializer,
        )
    }

    fun registerSharedTransition(
        source: KClass<out Destination>,
        target: KClass<out Destination>,
        groups: Set<SharedContentGroup> = setOf(SharedContentGroup.Default),
    ) {
        require(groups.isNotEmpty()) { "A shared transition must allow at least one content group" }
        sharedTransitions[SharedTransitionEdge(source = source, target = target)] = groups.toSet()
    }

    fun sharedTransitionGroups(source: Destination, target: Destination): Set<SharedContentGroup> =
        sharedTransitions[SharedTransitionEdge(source::class, target::class)].orEmpty()

    fun hasSharedTransition(
        source: Destination,
        target: Destination,
    ): Boolean = SharedTransitionEdge(source::class, target::class) in sharedTransitions

    private fun requireRegistration(destinationClass: KClass<out Destination>): ScreenEntry {
        return requireNotNull(registry[destinationClass]) {
            "Destination `${destinationClass.simpleName}` is not registered!"
        }
    }

    private fun requireRegistration(type: String): ScreenEntry {
        val entry = registry.entries.firstOrNull { (destinationClass, _) ->
            destinationClass.qualifiedName == type
        }
        return entry?.value ?: throw SerializationException("No serializer found for $type")
    }

    private class DestinationSerializer : KSerializer<Destination> {

        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Destination") {
            element<String>(TYPE_FIELD)
            element(PAYLOAD_FIELD, JsonElement.serializer().descriptor)
        }

        override fun serialize(encoder: Encoder, value: Destination) {
            val jsonEncoder = encoder as? JsonEncoder
                ?: throw SerializationException("Navigation destinations can only be serialized to JSON")

            val valueClass = value::class
            val registration = requireRegistration(valueClass)

            val type = valueClass.qualifiedName ?: throw SerializationException("No qualified name found for ${valueClass.simpleName}")
            val serializer = registration.serializer.cast()

            jsonEncoder.encodeJsonElement(
                buildJsonObject {
                    put(TYPE_FIELD, JsonPrimitive(type))
                    put(PAYLOAD_FIELD, jsonEncoder.json.encodeToJsonElement(serializer, value))
                }
            )
        }

        override fun deserialize(decoder: Decoder): Destination {
            val jsonDecoder = decoder as? JsonDecoder
                ?: throw SerializationException("Navigation destinations can only be deserialized from JSON")
            val jsonObject = decoder.decodeJsonElement() as? JsonObject
                ?: throw SerializationException("Expected JSON object for navigation destination")
            val type = jsonObject[TYPE_FIELD]?.jsonPrimitive?.contentOrNull
                ?: throw SerializationException("Missing destination type")
            val payload = jsonObject[PAYLOAD_FIELD]
                ?: throw SerializationException("Missing destination payload")

            val deserializer = requireRegistration(type).serializer.cast()
            return jsonDecoder.json.decodeFromJsonElement(deserializer, payload)
        }
    }

    private data class ScreenEntry(
        val screenClass: KClass<out Screen>,
        val serializer: KSerializer<out Destination>,
    )

    private data class SharedTransitionEdge(
        val source: KClass<out Destination>,
        val target: KClass<out Destination>,
    )
}

@Suppress("UNCHECKED_CAST")
private fun KSerializer<out Destination>.cast(): KSerializer<Destination> {
    return this as KSerializer<Destination>
}
