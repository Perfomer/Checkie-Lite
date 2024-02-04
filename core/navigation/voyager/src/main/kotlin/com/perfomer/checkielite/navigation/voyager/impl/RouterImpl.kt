package com.perfomer.checkielite.navigation.voyager.impl

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.Navigator
import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.navigation.voyager.impl.result.NavigationResultEventBus

internal class RouterImpl(
    private val navigatorHolder: NavigatorHolder,
    private val resultEventBus: NavigationResultEventBus,
    private val applicationExitProvider: ApplicationExitProvider,
) : Router {

    private val navigator: Navigator
        get() = navigatorHolder.navigator ?: error("navigator is not initialized")

    private val screenKey: ScreenKey
        get() = navigator.lastItem.key


    override fun navigate(screen: CheckieScreen) {
        navigator.push(screen as Screen)
    }

    override fun replace(screen: CheckieScreen) {
        navigator.replace(screen as Screen)
    }

    override suspend fun <T> navigateForResult(screen: CheckieScreen): T {
        navigate(screen)
        return resultEventBus.awaitResult((screen as Screen).key)
    }

    override fun exit() {
        if (!navigator.pop()) {
            applicationExitProvider.exit()
        }
    }

    override fun exitWithResult(result: Any?) {
        resultEventBus.sendResult(screenKey, result)
        exit()
    }
}