package com.perfomer.checkielite.core.data.util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

internal fun Context.startForegroundServiceCompat(intent: Intent) {
    ContextCompat.startForegroundService(this, intent)
}