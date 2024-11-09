package com.perfomer.checkielite.feature.main.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.dec.Destination
import kotlinx.serialization.Serializable

@Serializable
data object MainDestination : Destination()

fun interface MainScreenProvider {

    operator fun invoke(): CheckieScreen
}