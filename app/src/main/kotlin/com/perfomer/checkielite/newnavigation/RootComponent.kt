package com.perfomer.checkielite.newnavigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext,
    KoinComponent {

    private val navigation: StackNavigation<Destination> by inject()

    val childStack = childStack(
        source = navigation,
        serializer = Destination.serializer(),
        initialConfiguration = ADestination,
        handleBackButton = true,
        childFactory = ::createScreen,
    )

    fun onBackClicked() {
        navigation.pop()
    }

    private fun createScreen(destination: Destination, context: ComponentContext): BaseDecomposeScreen {
        val screenClass = NavigationRegistry.obtain(destination::class)
        return getKoin().get(screenClass, null) { parametersOf(context, destination) }
    }
}

interface BaseDecomposeScreen {
    @Composable
    fun Screen()
}

@Serializable
abstract class Destination

@Serializable
data object ADestination : Destination()

@Serializable
data class BDestination(val text: String) : Destination()
