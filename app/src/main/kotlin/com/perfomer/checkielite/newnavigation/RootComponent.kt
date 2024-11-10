package com.perfomer.checkielite.newnavigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.serialization.Serializable
import navigation.ScreenAComponent
import navigation.ScreenBComponent
import screens.ScreenA
import screens.ScreenB

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Destination>()

    fun onBackClicked() {
        navigation.pop()
    }

    val childStack = childStack(
        source = navigation,
        serializer = Destination.serializer(),
        initialConfiguration = ADestination,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(
        config: Destination,
        context: ComponentContext
    ): BaseDecomposeScreen {
        return when (config) {
            is ADestination -> ScreenA(
                ScreenAComponent(
                    componentContext = context,
                    onNavigateToScreenB = { text ->
                        navigation.pushNew(BDestination(text))
                    }
                )
            )
            is BDestination -> ScreenB(
                ScreenBComponent(
                    text = config.text,
                    componentContext = context,
                    onGoBack = {
                        navigation.pop()
                    }
                )
            )
            else -> throw RuntimeException()
        }
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
