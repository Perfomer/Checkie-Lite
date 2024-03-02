package com.perfomer.checkielite.navigation.voyager.impl

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.Navigator
import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.DestinationMode
import com.perfomer.checkielite.core.navigation.api.NoScreen
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.navigation.voyager.impl.result.NavigationResultEventBus

internal class RouterImpl(
    private val navigatorHolder: NavigatorHolder,
    private val resultEventBus: NavigationResultEventBus,
    private val applicationExitProvider: ApplicationExitProvider,
) : Router {

    private val navigator: Navigator
        get() = navigatorHolder.usualNavigator ?: error("Navigator is not initialized")

    private val overlayNavigator: Navigator
        get() = navigatorHolder.overlayNavigator ?: error("overlayNavigator is not initialized")

    private val isOverlayVisible: Boolean
        get() = overlayNavigator.lastItemOrNull !is NoScreen

    private val screenKey: ScreenKey
        get() = when {
            isOverlayVisible -> overlayNavigator.lastItem.key
            else -> navigator.lastItem.key
        }

    override fun navigate(screen: CheckieScreen, mode: DestinationMode) {
        when (mode) {
            DestinationMode.USUAL -> navigator.push(screen as Screen)
            DestinationMode.OVERLAY -> overlayNavigator.push(screen as Screen)
        }
    }

    override fun replace(screen: CheckieScreen, mode: DestinationMode) {
        when (mode) {
            DestinationMode.USUAL -> navigator.replace(screen as Screen)
            DestinationMode.OVERLAY -> overlayNavigator.replace(screen as Screen)
        }
    }

    override suspend fun <T> navigateForResult(screen: CheckieScreen, mode: DestinationMode): T {
        navigate(screen, mode)
        return resultEventBus.awaitResult((screen as Screen).key)
    }

    override fun exit() {
        when {
            isOverlayVisible -> overlayNavigator.pop()
            else -> if (!navigator.pop()) {
                applicationExitProvider.exit()
            }
        }
    }

    override fun exitWithResult(result: Any?) {
        resultEventBus.sendResult(screenKey, result)
        exit()
    }
}