package com.perfomer.checkielite.navigation.voyager

import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.navigation.voyager.impl.AndroidApplicationExitProvider
import com.perfomer.checkielite.navigation.voyager.impl.ApplicationExitProvider
import com.perfomer.checkielite.navigation.voyager.impl.NavigatorHolder
import com.perfomer.checkielite.navigation.voyager.impl.RouterImpl
import com.perfomer.checkielite.navigation.voyager.impl.result.NavigationResultEventBus
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val navigationModule = module {
	singleOf(::RouterImpl) bind Router::class
	singleOf(::AndroidApplicationExitProvider) bind ApplicationExitProvider::class
	singleOf(::NavigationResultEventBus)
	singleOf(::NavigatorHolder)
}