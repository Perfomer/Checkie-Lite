package com.perfomer.checkielite.navigation.voyager.impl

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.Navigator
import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.DestinationMode
import com.perfomer.checkielite.core.navigation.api.NoScreen
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.navigation.voyager.impl.result.NavigationResultEventBus
import com.perfomer.checkielite.navigation.voyager.navigator.BottomSheetNavigator

internal class RouterImpl(
    private val navigatorHolder: NavigatorHolder,
    private val resultEventBus: NavigationResultEventBus,
    private val applicationExitProvider: ApplicationExitProvider,
) : Router {

    private val navigator: Navigator
        get() = navigatorHolder.usualNavigator ?: error("Navigator is not initialized")

    private val bottomSheetNavigator: BottomSheetNavigator
        get() = navigatorHolder.bottomSheetNavigator ?: error("bottomSheetNavigator is not initialized")

    private val overlayNavigator: Navigator
        get() = navigatorHolder.overlayNavigator ?: error("overlayNavigator is not initialized")

    private val isBottomSheetVisible: Boolean
        get() = bottomSheetNavigator.isVisible

    private val isOverlayVisible: Boolean
        get() = overlayNavigator.lastItemOrNull !is NoScreen

    private val screenKey: ScreenKey
        get() = when {
            isBottomSheetVisible -> bottomSheetNavigator.lastItemOrNull?.key ?: error("bottomSheetNavigator screen is not provided")
            isOverlayVisible -> overlayNavigator.lastItem.key
            else -> navigator.lastItem.key
        }

    override fun navigate(screen: CheckieScreen, mode: DestinationMode) {
        when (mode) {
            DestinationMode.USUAL -> navigator.push(screen as Screen)
            DestinationMode.BOTTOM_SHEET -> bottomSheetNavigator.show(screen as Screen)
            DestinationMode.OVERLAY -> overlayNavigator.push(screen as Screen)
        }
    }

    override fun replace(screen: CheckieScreen, mode: DestinationMode) {
        when (mode) {
            DestinationMode.USUAL -> navigator.replace(screen as Screen)
            DestinationMode.BOTTOM_SHEET -> bottomSheetNavigator.replace(screen as Screen)
            DestinationMode.OVERLAY -> overlayNavigator.replace(screen as Screen)
        }
    }

    override fun replaceStack(screen: CheckieScreen) {
        navigator.popUntilRoot()
        navigator.replace(screen as Screen)
    }

    override suspend fun <T> navigateForResult(screen: CheckieScreen, mode: DestinationMode): T {
        navigate(screen, mode)
        return resultEventBus.awaitResult((screen as Screen).key)
    }

    override fun exit() {
        when {
            isOverlayVisible -> overlayNavigator.pop()
            isBottomSheetVisible -> bottomSheetNavigator.hide() // TODO: Think about pop here
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