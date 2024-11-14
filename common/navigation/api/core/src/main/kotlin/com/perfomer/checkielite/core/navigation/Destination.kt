package com.perfomer.checkielite.core.navigation

import kotlinx.serialization.Serializable

@Serializable
abstract class Destination {
    open val resultKey: String = this::class.qualifiedName.orEmpty()
}