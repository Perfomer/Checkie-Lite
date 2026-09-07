package com.perfomer.checkielite.core.navigation

/** A type-safe key for ephemeral data attached to a navigation entry. */
class NavigationDataKey<T : Any>

/**
 * Non-serializable data available while a destination is created.
 *
 * This keeps rendering hints and preloaded content out of the destination identity and saved state.
 */
class NavigationData private constructor(
    private val values: Map<NavigationDataKey<*>, Any>,
) {

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(key: NavigationDataKey<T>): T? = values[key] as T?

    val isEmpty: Boolean
        get() = values.isEmpty()

    class Builder internal constructor() {

        private val values = mutableMapOf<NavigationDataKey<*>, Any>()

        fun <T : Any> put(key: NavigationDataKey<T>, value: T) {
            values[key] = value
        }

        internal fun build(): NavigationData = NavigationData(values.toMap())
    }

    companion object {
        val Empty: NavigationData = NavigationData(emptyMap())
    }
}

fun navigationData(block: NavigationData.Builder.() -> Unit): NavigationData =
    NavigationData.Builder().apply(block).build()
