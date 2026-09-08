package com.perfomer.checkielite.navigation.decompose

import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.DestinationMode
import com.perfomer.checkielite.core.navigation.DestinationWithResult
import com.perfomer.checkielite.core.navigation.InitialContent
import com.perfomer.checkielite.core.navigation.Result
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.navigation.result.NavigationResultEventBus

internal class DecomposeRouter(
    private val resultEventBus: NavigationResultEventBus,
) : Router {

    private val root: DecomposeRootComponent by DecomposeRootComponentHolder

    override fun <D : Destination> navigate(
        destination: D,
        mode: DestinationMode,
        initialContent: InitialContent<D>?,
    ) = with(root) {
        withInitialContent(destination, initialContent) {
            when (mode) {
                DestinationMode.USUAL -> mainNavigator.pushToFront(destination)
                DestinationMode.OVERLAY -> overlayNavigator.activate(destination)
                DestinationMode.BOTTOM_SHEET -> bottomSheetNavigator.activate(destination)
            }
        }
    }

    override fun <D : Destination> replace(
        destination: D,
        mode: DestinationMode,
        initialContent: InitialContent<D>?,
    ) = with(root) {
        if (defineTopDestinationMode() != mode) {
            exit()
            navigate(destination, mode, initialContent)
            return@with
        }

        withInitialContent(destination, initialContent) {
            when (mode) {
                DestinationMode.USUAL -> mainNavigator.replaceCurrent(destination)
                DestinationMode.OVERLAY -> overlayNavigator.activate(destination)
                DestinationMode.BOTTOM_SHEET -> bottomSheetNavigator.activate(destination)
            }
        }
    }

    override fun <D : Destination> replaceStack(destination: D, initialContent: InitialContent<D>?) = with(root) {
        bottomSheetNavigator.dismiss()
        overlayNavigator.dismiss()
        withInitialContent(destination, initialContent) {
            mainNavigator.replaceAll(destination)
        }
    }

    override suspend fun <T : Result, D : DestinationWithResult<T>> navigateForResult(
        destination: D,
        mode: DestinationMode,
        initialContent: InitialContent<D>?,
    ): T? {
        navigate(destination, mode, initialContent)
        return resultEventBus.awaitResult(destination.resultKey)
    }

    override fun exit() {
        val topDestination = defineTopDestination()
        if (topDestination is DestinationWithResult<*>) {
            val resultKey = topDestination.resultKey
            resultEventBus.cancelResult(resultKey)
        }
        exitInternal()
    }

    override fun exitWithResult(result: Result) {
        val topDestination = defineTopDestination()
        if (topDestination is DestinationWithResult<*>) {
            val resultKey = topDestination.resultKey
            resultEventBus.sendResult(resultKey, result)
        }
        return exitInternal()
    }

    private fun exitInternal() = with(root) {
        when (defineTopDestinationMode()) {
            DestinationMode.USUAL -> mainNavigator.pop()
            DestinationMode.OVERLAY -> overlayNavigator.dismiss()
            DestinationMode.BOTTOM_SHEET -> bottomSheetNavigator.dismiss()
        }
    }

    private fun defineTopDestination(): Destination = when (defineTopDestinationMode()) {
        DestinationMode.USUAL -> root.mainNavigationStack.actual
        DestinationMode.OVERLAY -> requireNotNull(root.overlaySlot.active)
        DestinationMode.BOTTOM_SHEET -> requireNotNull(root.bottomSheetSlot.active)
    }

    private fun defineTopDestinationMode(): DestinationMode = with(root) {
        return when {
            bottomSheetSlot.isVisible -> DestinationMode.BOTTOM_SHEET
            overlaySlot.isVisible -> DestinationMode.OVERLAY
            else -> DestinationMode.USUAL
        }
    }
}
