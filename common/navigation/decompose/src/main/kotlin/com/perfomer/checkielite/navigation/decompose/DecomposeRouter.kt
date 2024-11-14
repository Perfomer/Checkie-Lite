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

    private val isBottomSheetVisible: Boolean
        get() = root.bottomSheetSlot.value.child != null

    private val isOverlayVisible: Boolean
        get() = root.overlaySlot.value.child != null

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
        return resultEventBus.awaitResult(destination.resultKey)
    }

    override fun exit() = with(root) {
        when {
            isBottomSheetVisible -> bottomSheetNavigator.dismiss()
            isOverlayVisible -> overlayNavigator.dismiss()
            else -> mainNavigator.pop()
        }
    }

    override fun exitWithResult(result: Any?) = with(root) {
        val resultKey = when {
            isBottomSheetVisible -> bottomSheetSlot.value.child!!.configuration.resultKey
            isOverlayVisible -> overlaySlot.value.child!!.configuration.resultKey
            else -> mainNavigationStack.value.active.configuration.resultKey
        }

        resultEventBus.sendResult(resultKey, result)
        exit()
    }
}