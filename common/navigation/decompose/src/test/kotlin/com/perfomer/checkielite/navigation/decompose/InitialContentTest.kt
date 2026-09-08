package com.perfomer.checkielite.navigation.decompose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.InitialContent
import com.perfomer.checkielite.core.navigation.InitialContentHolder
import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.core.navigation.Screen
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal class InitialContentTest {

    @BeforeEach
    fun setUp() {
        NavigationRegistry.register(TestDestination::class, TestDestinationSerializer, TestScreen::class)
        startKoin {
            modules(module {
                factoryOf(::TestStore)
                factoryOf(::TestScreen)
            })
        }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `content reaches the nested screen dependency only on creation`() {
        val root = root()
        assertNull(root.activeContent())
        val destination = TestDestination(1)
        val content = TestContent()

        root.withInitialContent(destination, content) {
            root.mainNavigator.pushToFront(destination)
        }
        assertSame(content, root.activeContent())

        root.mainNavigator.pop()
        root.mainNavigator.pushToFront(destination)
        assertNull(root.activeContent())
    }

    @Test
    fun `unused content does not leak into a later navigation`() {
        val root = root()
        val destination = TestDestination(1)

        root.withInitialContent(destination, TestContent()) {}
        root.mainNavigator.pushToFront(destination)

        assertNull(root.activeContent())
    }

    private fun root() = DecomposeRootComponent(
        componentContext = DefaultComponentContext(LifecycleRegistry()),
        startDestination = TestDestination(0),
    )

    private fun DecomposeRootComponent.activeContent(): TestContent? =
        (mainNavigationStack.value.active.instance as TestScreen).store.content.value

    private data class TestDestination(val id: Int) : Destination()

    private class TestContent : InitialContent<TestDestination>

    private class TestStore(val content: InitialContentHolder<TestContent>)

    private class TestScreen(val store: TestStore) : Screen {
        @Composable
        override fun Screen() = Unit
    }

    private object TestDestinationSerializer : KSerializer<TestDestination> {
        override val descriptor = Int.serializer().descriptor
        override fun serialize(encoder: Encoder, value: TestDestination) = encoder.encodeInt(value.id)
        override fun deserialize(decoder: Decoder) = TestDestination(decoder.decodeInt())
    }
}
