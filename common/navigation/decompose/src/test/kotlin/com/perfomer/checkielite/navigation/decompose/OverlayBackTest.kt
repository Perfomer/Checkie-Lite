package com.perfomer.checkielite.navigation.decompose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.backhandler.BackEvent
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.core.navigation.Screen
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

internal class OverlayBackTest {
    @BeforeEach
    fun setUp() {
        NavigationRegistry.register(TestDestination::class, Serializer, TestScreen::class)
        startKoin { modules(module { factory { TestScreen() } }) }
    }

    @AfterEach
    fun tearDown() = stopKoin()

    @Test
    fun `first back dismisses overlay and only the next back pops the main stack`() {
        val dispatcher = BackDispatcher()
        val root = root(dispatcher)
        root.mainNavigator.pushToFront(TestDestination(1))
        root.overlayNavigator.activate(TestDestination(2))

        assertTrue(dispatcher.back())
        assertNull(root.overlaySlot.value.child)
        assertEquals(TestDestination(1), root.mainNavigationStack.value.active.configuration)
        assertEquals(1, root.mainNavigationStack.value.backStack.size)

        assertTrue(dispatcher.back())
        assertEquals(TestDestination(0), root.mainNavigationStack.value.active.configuration)
    }

    @Test
    fun `overlay on root consumes back and remains dismissible after reopening`() {
        val dispatcher = BackDispatcher()
        val root = root(dispatcher)
        repeat(2) {
            root.overlayNavigator.activate(TestDestination(2))
            assertTrue(dispatcher.back())
            assertNull(root.overlaySlot.value.child)
            assertEquals(TestDestination(0), root.mainNavigationStack.value.active.configuration)
        }
    }

    @Test
    fun `cancelled predictive back leaves overlay and main stack intact`() {
        val dispatcher = BackDispatcher()
        val root = root(dispatcher)
        root.mainNavigator.pushToFront(TestDestination(1))
        root.overlayNavigator.activate(TestDestination(2))
        dispatcher.startPredictiveBack(BackEvent())
        dispatcher.progressPredictiveBack(BackEvent(progress = 0.5F))
        dispatcher.cancelPredictiveBack()
        assertNotNull(root.overlaySlot.value.child)
        assertEquals(TestDestination(1), root.mainNavigationStack.value.active.configuration)
        assertTrue(dispatcher.back())
        assertNull(root.overlaySlot.value.child)
        assertEquals(TestDestination(1), root.mainNavigationStack.value.active.configuration)
    }

    private fun root(dispatcher: BackDispatcher) = DecomposeRootComponent(
        componentContext = DefaultComponentContext(
            lifecycle = LifecycleRegistry().apply { resume() },
            backHandler = dispatcher,
        ),
        startDestination = TestDestination(0),
    )

    private data class TestDestination(val id: Int) : Destination()

    private class TestScreen : Screen {
        @Composable
        override fun Screen() = Unit
    }

    private object Serializer : KSerializer<TestDestination> {
        override val descriptor = Int.serializer().descriptor
        override fun serialize(encoder: Encoder, value: TestDestination) = encoder.encodeInt(value.id)
        override fun deserialize(decoder: Decoder) = TestDestination(decoder.decodeInt())
    }
}
