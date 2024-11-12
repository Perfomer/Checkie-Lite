package com.perfomer.checkielite.navigation.di

import com.perfomer.checkielite.core.navigation.NavigationHost
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.navigation.decompose.DecomposeNavigationHost
import com.perfomer.checkielite.navigation.decompose.DecomposeNavigatorHolder
import com.perfomer.checkielite.navigation.decompose.DecomposeRouter
import com.perfomer.checkielite.navigation.result.NavigationResultEventBus
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val navigationModule = module {
    single { DecomposeNavigatorHolder() }

    factoryOf(::DecomposeNavigationHost) bind NavigationHost::class
    singleOf(::DecomposeRouter) bind Router::class

    singleOf(::NavigationResultEventBus)
}