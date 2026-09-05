package com.perfomer.checkielite.common.ui.cui.widget.toast

import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastData.Companion.DEFAULT_DURATION
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import kotlin.time.Duration

fun ToastController.showToast(
    message: Text,
    style: ToastStyle = ToastStyle.NEUTRAL,
    duration: Duration = DEFAULT_DURATION,
) {
    showToast(ToastData(message, style, duration))
}