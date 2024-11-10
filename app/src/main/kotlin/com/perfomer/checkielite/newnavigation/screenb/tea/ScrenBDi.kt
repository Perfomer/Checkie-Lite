package com.perfomer.checkielite.newnavigation.screenb.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.newnavigation.BDestination
import com.perfomer.checkielite.newnavigation.screenb.ScreenB
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val screenBModule = module {
    factory { params -> ScreenB(get { params }) }
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