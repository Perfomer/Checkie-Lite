package com.perfomer.checkielite.navigation.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.core.navigation.Screen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

internal class DecomposeRootComponent(
    componentContext: ComponentContext,
    startDestination: Destination,
) : ComponentContext by componentContext,
    KoinComponent {

    private val navigation: StackNavigation<Destination> by inject()

    val childStack = childStack(
        source = navigation,
        serializer = NavigationRegistry.serializer(),
        initialConfiguration = startDestination,
        handleBackButton = true,
        childFactory = ::createScreen,
    )

    fun onBackClicked() {
        navigation.pop()
    }

    private fun createScreen(destination: Destination, context: ComponentContext): Screen {
        val screenClass = NavigationRegistry.obtain(destination::class)
        return getKoin().get(screenClass, null) { parametersOf(context, destination) }
    }
}