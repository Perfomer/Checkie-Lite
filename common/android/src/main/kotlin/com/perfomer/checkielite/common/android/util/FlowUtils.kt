package com.perfomer.checkielite.common.android.util

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

fun <T> Flow<T>.onCatchLog(
    tag: String,
    message: String,
    rethrow: Boolean = true,
) = catch { error ->
    Log.e(tag, message, error)
    if (rethrow) throw error
}