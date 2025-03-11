package com.perfomer.checkielite.navigation.decompose

import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.DestinationMode
import com.perfomer.checkielite.core.navigation.Result
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.navigation.result.NavigationResultEventBus

internal class DecomposeRouter(
    private val resultEventBus: NavigationResultEventBus,
) : Router {

    private val root: DecomposeRootComponent by DecomposeRootComponentHolder

    override fun navigate(destination: Destination, mode: DestinationMode) = with(root) {
        when (mode) {
            DestinationMode.USUAL -> mainNavigator.pushToFront(destination)
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

    override suspend fun <T : Result> navigateForResult(
        destination: Destination,
        mode: DestinationMode,
    ): T {
        navigate(destination, mode)
        return resultEventBus.awaitResult(destination.resultKey)
    }

    override fun exit() = with(root) {
        when {
            bottomSheetSlot.isVisible -> bottomSheetNavigator.dismiss()
            overlaySlot.isVisible -> overlayNavigator.dismiss()
            else -> mainNavigator.pop()
        }
    }

    override fun exitWithResult(result: Result) = with(root) {
        val resultKey = when {
            bottomSheetSlot.isVisible -> bottomSheetSlot.active!!.resultKey
            overlaySlot.isVisible -> overlaySlot.active!!.resultKey
            else -> mainNavigationStack.actual.resultKey
        }

        resultEventBus.sendResult(resultKey, result)
        exit()
    }
}