package com.perfomer.checkielite.newnavigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.perfomer.checkielite.newnavigation.screena.ScreenA
import com.perfomer.checkielite.newnavigation.screenb.ScreenB
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext,
    KoinComponent {

    private val navigation: StackNavigation<Destination> by inject()

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
        val screenClass = navigationAssociation[destination.javaClass.kotlin]!!

        return getKoin().get(screenClass, null) { parametersOf(context, destination) }
    }
}

val navigationAssociation = mutableMapOf<KClass<out Destination>, KClass<out BaseDecomposeScreen>>(
    ADestination::class to ScreenA::class,
    BDestination::class to ScreenB::class,
)

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
