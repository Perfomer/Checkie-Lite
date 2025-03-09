package com.perfomer.checkielite.common.ui.util.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf

val LocalBottomSheetDismissHandlerOwner = staticCompositionLocalOf<BottomSheetDismissHandlerOwner> { error("Not provided") }

@Composable
fun BottomSheetDismissHandler(
    enabled: Boolean = true,
    onDismissRequested: () -> Boolean,
) {
    val dismissHandlerOwner = requireNotNull(LocalBottomSheetDismissHandlerOwner.current)
    val currentEnabled by rememberUpdatedState(enabled)
    val currentOnDismissRequested by rememberUpdatedState(onDismissRequested)
    val dismissCallback = remember {
        object : BottomSheetDismissCallback {
            override fun onDismissRequested(): Boolean {
                return if (currentEnabled) currentOnDismissRequested()
                else true
            }
        }
    }

    DisposableEffect(dismissHandlerOwner) {
        dismissHandlerOwner.add(dismissCallback)

        onDispose {
            dismissHandlerOwner.remove(dismissCallback)
        }
    }
}

interface BottomSheetDismissHandlerOwner {

    val current: BottomSheetDismissCallback

    fun add(handler: BottomSheetDismissCallback)
    fun remove(handler: BottomSheetDismissCallback)
}

class DefaultBottomSheetDismissHandlerOwner : BottomSheetDismissHandlerOwner {

    private val handlers = mutableListOf<BottomSheetDismissCallback>()

    override val current: BottomSheetDismissCallback
        get() = handlers.lastOrNull() ?: NoOpBottomSheetDismissCallback

    override fun add(handler: BottomSheetDismissCallback) {
        handlers += handler
    }

    override fun remove(handler: BottomSheetDismissCallback) {
        handlers -= handler
    }

    private object NoOpBottomSheetDismissCallback : BottomSheetDismissCallback {
        override fun onDismissRequested(): Boolean = true
    }
}

interface BottomSheetDismissCallback {

    /**
     * Returns true if the dismiss request is allowed
     */
    fun onDismissRequested(): Boolean
}