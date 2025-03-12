package com.perfomer.checkielite.common.ui.cui.widget.toast

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

val LocalToastController = staticCompositionLocalOf<ToastController> { error("Not provided") }

@Stable
class ToastController {

    var currentToastData by mutableStateOf<ToastData?>(null)
        private set

    var show by mutableStateOf(false)
        private set

    fun showToast(data: ToastData) {
        currentToastData = data
        show = true
    }

    fun dismiss() {
        show = false
    }
}