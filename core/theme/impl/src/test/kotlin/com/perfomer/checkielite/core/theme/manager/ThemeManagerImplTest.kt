package com.perfomer.checkielite.core.theme.manager

import com.perfomer.checkielite.core.data.repository.ThemeRepository
import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode
import java.io.IOException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ThemeManagerImplTest {

    @Test
    fun `glass is enabled by default`() = runTest {
        val manager = ThemeManagerImpl(FakeThemeRepository())

        manager.warmUp()

        assertTrue(manager.isLiquidGlassEnabled.value)
        assertEquals(ThemeMode.SYSTEM, manager.themeMode.value)
    }

    @Test
    fun `saved disabled glass is restored with the selected theme`() = runTest {
        val manager = ThemeManagerImpl(FakeThemeRepository(ThemeMode.DARK, false))

        manager.warmUp()

        assertFalse(manager.isLiquidGlassEnabled.value)
        assertEquals(ThemeMode.DARK, manager.themeMode.value)
    }

    @Test
    fun `changes reach every observer and survive a new manager`() = runTest {
        val repository = FakeThemeRepository(ThemeMode.LIGHT)
        val manager = ThemeManagerImpl(repository)
        manager.warmUp()
        val firstObserver = mutableListOf<Boolean>()
        val secondObserver = mutableListOf<Boolean>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            manager.isLiquidGlassEnabled.collect { firstObserver.add(it) }
        }
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            manager.isLiquidGlassEnabled.collect { secondObserver.add(it) }
        }

        manager.setLiquidGlassEnabled(false)
        manager.setLiquidGlassEnabled(true)
        manager.setLiquidGlassEnabled(false)
        val restartedManager = ThemeManagerImpl(repository)
        restartedManager.warmUp()

        assertEquals(listOf(true, false, true, false), firstObserver)
        assertEquals(firstObserver, secondObserver)
        assertFalse(restartedManager.isLiquidGlassEnabled.value)
        assertEquals(ThemeMode.LIGHT, restartedManager.themeMode.value)
    }

    @Test
    fun `failed save leaves the published value unchanged`() = runTest {
        val repository = FakeThemeRepository().apply { failSaving = true }
        val manager = ThemeManagerImpl(repository)

        val error = runCatching { manager.setLiquidGlassEnabled(false) }.exceptionOrNull()

        assertInstanceOf(IOException::class.java, error)
        assertTrue(manager.isLiquidGlassEnabled.value)
    }

    @Test
    fun `leaving settings during a save still publishes the persisted value`() = runTest {
        val writeGate = CompletableDeferred<Unit>()
        val repository = FakeThemeRepository().apply { this.writeGate = writeGate }
        val manager = ThemeManagerImpl(repository)
        val save = launch { manager.setLiquidGlassEnabled(false) }
        runCurrent()

        save.cancel()
        writeGate.complete(Unit)
        save.join()

        assertFalse(repository.isLiquidGlassEnabled())
        assertFalse(manager.isLiquidGlassEnabled.value)
    }

    @Test
    fun `a pending warmup cannot overwrite a later toggle`() = runTest {
        val readGate = CompletableDeferred<Unit>()
        val repository = FakeThemeRepository(liquidGlassEnabled = false).apply { this.readGate = readGate }
        val manager = ThemeManagerImpl(repository)
        val warmUp = launch { manager.warmUp() }
        runCurrent()
        val toggle = launch { manager.setLiquidGlassEnabled(true) }
        runCurrent()

        readGate.complete(Unit)
        warmUp.join()
        toggle.join()

        assertTrue(manager.isLiquidGlassEnabled.value)
        assertTrue(repository.isLiquidGlassEnabled())
    }

    private class FakeThemeRepository(
        private var themeMode: ThemeMode? = null,
        private var liquidGlassEnabled: Boolean = true,
    ) : ThemeRepository {

        var failSaving = false
        var readGate: CompletableDeferred<Unit>? = null
        var writeGate: CompletableDeferred<Unit>? = null

        override suspend fun getThemeMode(): ThemeMode? = themeMode

        override suspend fun setThemeMode(themeMode: ThemeMode) {
            this.themeMode = themeMode
        }

        override suspend fun isLiquidGlassEnabled(): Boolean {
            readGate?.await()
            return liquidGlassEnabled
        }

        override suspend fun setLiquidGlassEnabled(enabled: Boolean) {
            if (failSaving) throw IOException("Write failed")
            writeGate?.await()
            liquidGlassEnabled = enabled
        }
    }
}
