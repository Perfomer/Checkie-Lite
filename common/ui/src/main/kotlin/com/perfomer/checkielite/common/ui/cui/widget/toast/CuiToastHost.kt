package com.perfomer.checkielite.common.ui.cui.widget.toast

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.perfomer.checkielite.common.ui.cui.widget.toast.CuiToastHostState.Companion.DEFAULT_DURATION_MS
import kotlinx.coroutines.delay

val LocalCuiToastHostState = staticCompositionLocalOf<CuiToastHostState> { error("Not provided") }

@Stable
class CuiToastHostState {

    var currentToastData by mutableStateOf<CuiToastData?>(null)
        private set

    var show by mutableStateOf(false)
        private set


    fun showToast(
        message: String,
        icon: Painter,
        durationMs: Long = DEFAULT_DURATION_MS,
    ) {
        showToast(CuiToastData(message, icon, durationMs))
    }

    fun showToast(data: CuiToastData) {
        currentToastData = data
        show = true
    }

    fun dismiss() {
        show = false
    }

    internal companion object {
        internal const val DEFAULT_DURATION_MS = 5_000L
    }
}

@Composable
fun rememberToast(
    @StringRes message: Int,
    @DrawableRes icon: Int,
    durationMs: Long = DEFAULT_DURATION_MS,
): CuiToastData {
    val actualMessage = stringResource(message)
    val actualIcon = painterResource(icon)

    return remember(message, icon) {
        CuiToastData(actualMessage, actualIcon, durationMs)
    }
}

@Stable
data class CuiToastData(
    val message: String,
    val icon: Painter,
    val durationMs: Long,
)

@Composable
fun CuiToastHost(
    hostState: CuiToastHostState = LocalCuiToastHostState.current,
    toast: @Composable (CuiToastData) -> Unit = { data ->
        CuiToast(
            data = data,
            onClick = { hostState.dismiss() },
        )
    },
) {
    val currentToastData = hostState.currentToastData

    LaunchedEffect(hostState.show) {
        if (currentToastData != null && hostState.show) {
            delay(currentToastData.durationMs)
            hostState.dismiss()
        }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = hostState.show && currentToastData != null,
            enter = slideInVertically(),
            exit = slideOutVertically(targetOffsetY = { -it }),
        ) {
            toast(currentToastData!!)
        }
    }
}