package com.perfomer.checkielite.navigation.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.core.navigation.Screen
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class DecomposeRootComponent(
    componentContext: ComponentContext,
    startDestination: Destination,
) : ComponentContext by componentContext,
    KoinComponent {

    val mainNavigator: StackNavigation<Destination> = StackNavigation()
    val bottomSheetNavigator: SlotNavigation<Destination> = SlotNavigation()
    val overlayNavigator: SlotNavigation<Destination> = SlotNavigation()

    val mainNavigationStack = childStack(
        source = mainNavigator,
        serializer = NavigationRegistry.serializer(),
        initialConfiguration = startDestination,
        handleBackButton = true,
        childFactory = ::createScreen,
    )

    val bottomSheetSlot = childSlot(
        source = bottomSheetNavigator,
        serializer = NavigationRegistry.serializer(),
        key = "BottomSheet",
        childFactory = ::createScreen,
    )

    val overlaySlot = childSlot(
        source = overlayNavigator,
        serializer = NavigationRegistry.serializer(),
        key = "Overlay",
        childFactory = ::createScreen,
    )

    private fun createScreen(destination: Destination, context: ComponentContext): Screen {
        val screenClass = NavigationRegistry.obtain(destination::class)
        return getKoin().get(screenClass, null) { parametersOf(context, destination) }
    }
}

internal object DecomposeRootComponentHolder : ReadWriteProperty<Any, DecomposeRootComponent> {

    private lateinit var rootComponent: DecomposeRootComponent

    override fun getValue(thisRef: Any, property: KProperty<*>) = get()
    override fun setValue(thisRef: Any, property: KProperty<*>, value: DecomposeRootComponent) = set(value)

    fun get(): DecomposeRootComponent {
        return rootComponent
    }

    fun set(rootComponent: DecomposeRootComponent) {
        this.rootComponent = rootComponent
    }
}
