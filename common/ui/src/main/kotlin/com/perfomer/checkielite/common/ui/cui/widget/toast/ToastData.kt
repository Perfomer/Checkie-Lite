package com.perfomer.checkielite.common.ui.cui.widget.toast

import androidx.compose.runtime.Stable
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Stable
data class ToastData(
    val message: Text,
    val style: ToastStyle,
    val duration: Duration = DEFAULT_DURATION,
) {
    companion object {
        val DEFAULT_DURATION = 5.seconds
    }
}