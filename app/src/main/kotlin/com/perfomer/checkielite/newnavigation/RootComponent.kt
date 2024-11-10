package com.perfomer.checkielite.newnavigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.perfomer.checkielite.newnavigation.screena.ScreenA
import com.perfomer.checkielite.newnavigation.screena.ScreenAComponent
import com.perfomer.checkielite.newnavigation.screenb.ScreenB
import com.perfomer.checkielite.newnavigation.screenb.tea.ScreenBStore
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext,
    KoinComponent {

    private val navigation = StackNavigation<Destination>()

    fun onBackClicked() {
        navigation.pop()
    }

    val childStack = childStack(
        source = navigation,
        serializer = Destination.serializer(),
        initialConfiguration = ADestination,
        handleBackButton = true,
        childFactory = ::createChild,
    )

    private fun createChild(
        destination: Destination,
        context: ComponentContext
    ): BaseDecomposeScreen {
        return when (destination) {
            is ADestination -> ScreenA(
                ScreenAComponent(
                    componentContext = context,
                    onNavigateToScreenB = { text ->
                        navigation.pushNew(BDestination(text))
                    }
                )
            )
            is BDestination -> ScreenB(
                store = get<ScreenBStore> { parametersOf(context, destination) },
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
