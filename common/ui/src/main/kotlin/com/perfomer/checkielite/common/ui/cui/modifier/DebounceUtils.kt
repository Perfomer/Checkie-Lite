package com.perfomer.checkielite.common.ui.cui.modifier

import android.os.SystemClock
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 * Wraps an [onClick] lambda with another one that supports debouncing. The default deboucing time
 * is 1000ms.
 *
 * @return debounced onClick
 */
@Composable
inline fun debounced(crossinline onClick: () -> Unit, debounceTime: Long = 1000L): () -> Unit {
    var lastTimeClicked by remember { mutableLongStateOf(0L) }

    return {
        val now = SystemClock.uptimeMillis()
        if (now - lastTimeClicked > debounceTime) { onClick() }

        lastTimeClicked = now
    }
}

/**
 * The same as [Modifier.clickable] with support to debouncing.
 */
fun Modifier.debouncedClickable(
    debounceTime: Long = 1000L,
    onClick: () -> Unit,
): Modifier {
    return this.composed {
        val clickable = debounced(debounceTime = debounceTime, onClick = onClick)
        this.clickable(onClick = clickable)
    }
}