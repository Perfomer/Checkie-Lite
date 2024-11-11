package com.perfomer.checkielite.newnavigation

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.core.navigation.associate
import com.perfomer.checkielite.core.navigation.navigation
import com.perfomer.checkielite.newnavigation.screena.ScreenA
import com.perfomer.checkielite.newnavigation.screena.ScreenAComponent
import com.perfomer.checkielite.newnavigation.screenb.ScreenB
import com.perfomer.checkielite.newnavigation.screenb.tea.ScreenBStore
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val decomposeWipModule = module {
    navigation {
        associate<ADestination, ScreenA>()
        associate<BDestination, ScreenB>()
    }

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