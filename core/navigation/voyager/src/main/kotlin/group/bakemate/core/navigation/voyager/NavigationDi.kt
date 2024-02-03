@file:Suppress("RemoveExplicitTypeArguments")

package group.bakemate.core.navigation.voyager

import com.perfomer.checkielite.core.navigation.api.Router
import group.bakemate.core.navigation.voyager.impl.AndroidApplicationExitProvider
import group.bakemate.core.navigation.voyager.impl.ApplicationExitProvider
import group.bakemate.core.navigation.voyager.impl.NavigatorHolder
import group.bakemate.core.navigation.voyager.impl.RouterImpl
import group.bakemate.core.navigation.voyager.impl.result.NavigationResultEventBus
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val navigationModule = module {
	singleOf(::RouterImpl) bind Router::class
	singleOf(::AndroidApplicationExitProvider) bind ApplicationExitProvider::class
	singleOf(::NavigationResultEventBus)
	singleOf(::NavigatorHolder)
}