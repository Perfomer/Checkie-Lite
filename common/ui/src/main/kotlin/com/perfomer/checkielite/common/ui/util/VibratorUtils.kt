@file:SuppressLint("MissingPermission")
@file:Suppress("DEPRECATION")

package com.perfomer.checkielite.common.ui.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

object VibratorPattern {
    val ERROR = longArrayOf(50, 100, 50, 100, 50, 100)
}

@Composable
fun rememberVibrator(): Vibrator {
    val context = LocalContext.current
    return remember { context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }
}

fun Vibrator.vibrateCompat(pattern: LongArray) {
    cancel()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrate(VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrate(pattern, -1)
    }
}