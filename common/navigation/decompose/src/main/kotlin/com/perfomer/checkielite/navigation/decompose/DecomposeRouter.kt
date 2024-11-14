package com.perfomer.checkielite.navigation.decompose

import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.DestinationMode
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.navigation.result.NavigationResultEventBus

internal class DecomposeRouter(
    private val resultEventBus: NavigationResultEventBus,
) : Router {

    private val root: DecomposeRootComponent by DecomposeRootComponentHolder

    override fun navigate(destination: Destination, mode: DestinationMode) = with(root) {
        when (mode) {
            DestinationMode.USUAL -> mainNavigator.pushNew(destination)
            DestinationMode.OVERLAY -> overlayNavigator.activate(destination)
            DestinationMode.BOTTOM_SHEET -> bottomSheetNavigator.activate(destination)
        }
    }

    override fun replace(destination: Destination, mode: DestinationMode) = with(root) {
        when (mode) {
            DestinationMode.USUAL -> mainNavigator.replaceCurrent(destination)
            DestinationMode.OVERLAY -> overlayNavigator.activate(destination)
            DestinationMode.BOTTOM_SHEET -> bottomSheetNavigator.activate(destination)
        }
    }

    override fun replaceStack(destination: Destination) = with(root) {
        bottomSheetNavigator.dismiss()
        overlayNavigator.dismiss()
        mainNavigator.replaceAll(destination)
    }

    override suspend fun <T> navigateForResult(
        destination: Destination,
        mode: DestinationMode,
    ): T {
        navigate(destination, mode)
        // TODO: result key?
        return resultEventBus.awaitResult("")
    }

    override fun exit() = with(root) {
        when {
            bottomSheetSlot.value.child != null -> bottomSheetNavigator.dismiss()
            overlaySlot.value.child != null -> overlayNavigator.dismiss()
            else -> mainNavigator.pop()
        }
    }

    override fun exitWithResult(result: Any?) {
        // TODO: result key?
        resultEventBus.sendResult("", result)
        exit()
    }
}