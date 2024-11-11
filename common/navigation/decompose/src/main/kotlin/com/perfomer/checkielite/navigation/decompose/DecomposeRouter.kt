package com.perfomer.checkielite.navigation.decompose

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
    private val navigationStack: StackNavigation<Destination>,
    private val resultEventBus: NavigationResultEventBus,
) : Router {

    override fun navigate(destination: Destination, mode: DestinationMode) {
        // TODO: DestinationMode
        navigationStack.pushNew(destination)
    }

    override fun replace(destination: Destination, mode: DestinationMode) {
        // TODO: DestinationMode
        navigationStack.replaceCurrent(destination)
    }

    override fun replaceStack(destination: Destination) {
        navigationStack.replaceAll(destination)
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
        navigationStack.pop()
    }

    override fun exitWithResult(result: Any?) {
        // TODO: result key?
        resultEventBus.sendResult("", result)
        exit()
    }
}