package com.perfomer.checkielite.common.ui.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

fun Modifier.stableStatusBarsPadding() = composed {
    windowInsetsPadding(StableInsets.statusBars())
}

fun Modifier.stableNavigationBarsPadding() = composed {
    windowInsetsPadding(StableInsets.navigationBars())
}

object StableInsets {

    @Composable
    fun statusBars(): WindowInsets {
        return StableInsetsHolder.stableStatusBar()
    }

    @Composable
    fun navigationBars(): WindowInsets {
        return StableInsetsHolder.stableNavigationBar()
    }
}

internal object StableInsetsHolder {

    private var stableStatusBarInsets: MutableState<WindowInsets> = mutableStateOf(WindowInsets(0.dp))
    private var stableNavigationBarInsets: MutableState<WindowInsets> = mutableStateOf(WindowInsets(0.dp))

    @Composable
    internal fun stableStatusBar(): WindowInsets {
        return stableInsets(stableStatusBarInsets, WindowInsets.statusBars)
    }

    @Composable
    internal fun stableNavigationBar(): WindowInsets {
        return stableInsets(stableNavigationBarInsets, WindowInsets.navigationBars)
    }

    @Composable
    private fun stableInsets(
        insets: MutableState<WindowInsets>,
        targetInsets: WindowInsets,
    ): WindowInsets {
        var currentInsets by insets
        val density = LocalDensity.current
        val layoutDirection = LocalLayoutDirection.current

        return remember(targetInsets) {
            derivedStateOf {
                val difference = targetInsets.exclude(currentInsets)
                if (!difference.isEmpty(density, layoutDirection)) {
                    currentInsets = targetInsets.deepCopy(density, layoutDirection)
                }
                currentInsets
            }.value
        }
    }
}

private fun WindowInsets.isEmpty(density: Density, layoutDirection: LayoutDirection): Boolean {
    return getTop(density) == 0 &&
            getRight(density, layoutDirection) == 0 &&
            getBottom(density) == 0 &&
            getLeft(density, layoutDirection) == 0
}

private fun WindowInsets.deepCopy(density: Density, layoutDirection: LayoutDirection): WindowInsets {
    return WindowInsets(
        left = getLeft(density, layoutDirection),
        top = getTop(density),
        right = getRight(density, layoutDirection),
        bottom = getBottom(density)
    )
}