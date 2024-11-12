package com.perfomer.checkielite.navigation.decompose

import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.DestinationMode
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.navigation.result.NavigationResultEventBus

internal class DecomposeRouter(
    private val navigation: DecomposeNavigatorHolder,
    private val resultEventBus: NavigationResultEventBus,
) : Router {

    private val navigator: StackNavigation<Destination>
        get() = navigation.mainNavigator

    private val bottomSheetNavigator: SlotNavigation<Destination>
        get() = navigation.bottomSheetNavigator

    private val overlayNavigator: SlotNavigation<Destination>
        get() = navigation.overlayNavigator

    override fun navigate(destination: Destination, mode: DestinationMode) {
        when (mode) {
            DestinationMode.USUAL -> navigator.pushNew(destination)
            DestinationMode.OVERLAY -> TODO()
            DestinationMode.BOTTOM_SHEET -> bottomSheetNavigator.activate(destination)
        }

    }

    override fun replace(destination: Destination, mode: DestinationMode) {
        // TODO: DestinationMode
        navigator.replaceCurrent(destination)
    }

    override fun replaceStack(destination: Destination) {
        navigator.replaceAll(destination)
    }

    override suspend fun <T> navigateForResult(
        destination: Destination,
        mode: DestinationMode,
    ): T {
        navigate(destination, mode)
        // TODO: result key?
        return resultEventBus.awaitResult("")
    }

    override fun exit() {
        navigator.pop()
    }

    override fun exitWithResult(result: Any?) {
        // TODO: result key?
        resultEventBus.sendResult("", result)
        exit()
    }
}