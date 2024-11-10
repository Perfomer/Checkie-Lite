package com.perfomer.checkielite.newnavigation.screenb.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.newnavigation.BDestination
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val screenBModule = module {
    factoryOf(::createScreenBStore)
}

internal fun createScreenBStore(
    componentContext: ComponentContext,
    destination: BDestination,
): ScreenBStore {
    return ScreenBStore(
        componentContext = componentContext,
        params = destination,
    )
}