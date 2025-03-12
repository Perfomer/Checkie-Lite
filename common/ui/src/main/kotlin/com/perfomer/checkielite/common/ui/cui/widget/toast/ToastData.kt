package com.perfomer.checkielite.common.ui.cui.widget.toast

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

@Stable
data class ToastData(
    val message: String,
    val icon: Painter?,
    val iconTint: Color,
    val backgroundColor: Color,
    val durationMs: Long = DEFAULT_DURATION_MS,
) {
    companion object {
        const val DEFAULT_DURATION_MS = 5_000L
    }
}