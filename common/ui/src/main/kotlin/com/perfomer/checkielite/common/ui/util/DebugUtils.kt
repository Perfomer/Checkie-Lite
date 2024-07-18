package com.perfomer.checkielite.common.ui.util

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

fun Context.isDebug(): Boolean {
    return applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
}

@Composable
@ReadOnlyComposable
fun isDebug(): Boolean {
    val context = LocalContext.current
    return context.isDebug()
}