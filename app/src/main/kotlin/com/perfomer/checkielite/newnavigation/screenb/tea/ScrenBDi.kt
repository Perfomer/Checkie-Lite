package com.perfomer.checkielite.newnavigation.screenb.tea

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.perfomer.checkielite.newnavigation.BDestination
import com.perfomer.checkielite.newnavigation.Destination
import com.perfomer.checkielite.newnavigation.screena.ScreenA
import com.perfomer.checkielite.newnavigation.screena.ScreenAComponent
import com.perfomer.checkielite.newnavigation.screenb.ScreenB
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val decomposeModule = module {
    single { StackNavigation<Destination>() }

    factoryOf(::ScreenA)
    factoryOf(::ScreenB)

    factoryOf(::ScreenAComponent)
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