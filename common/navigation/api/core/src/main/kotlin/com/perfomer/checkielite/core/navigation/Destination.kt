package com.perfomer.checkielite.core.navigation

import kotlinx.serialization.Serializable

@Serializable
abstract class Destination

@Serializable
abstract class DestinationWithResult<T : Result> : Destination() {
    open val resultKey: String = this::class.qualifiedName.orEmpty()
}