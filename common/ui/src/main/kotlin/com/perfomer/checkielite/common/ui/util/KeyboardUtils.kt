package com.perfomer.checkielite.common.ui.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect

@Composable
fun keyboardOpenedAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

@Composable
fun ClearFocusOnKeyboardClose() {
    val focusManager = LocalFocusManager.current
    val isKeyboardOpened by keyboardOpenedAsState()

    UpdateEffect(isKeyboardOpened) {
        if (!isKeyboardOpened) focusManager.clearFocus()
    }
}