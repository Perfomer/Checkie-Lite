package com.perfomer.checkielite.newnavigation

import com.perfomer.checkielite.core.navigation.Destination
import kotlinx.serialization.Serializable

@Serializable
data object ADestination : Destination()

@Serializable
data class BDestination(val text: String) : Destination()
