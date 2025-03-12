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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastController.Companion.DEFAULT_DURATION_MS
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import kotlinx.coroutines.delay

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

    internal companion object {
        internal const val DEFAULT_DURATION_MS = 5_000L
    }
}

@Composable
fun rememberToast(
    @StringRes message: Int,
    @DrawableRes icon: Int? = null,
    iconTint: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    durationMs: Long = DEFAULT_DURATION_MS,
): ToastData {
    val actualMessage = stringResource(message)
    val actualIcon = icon?.let { painterResource(it) }

    return remember(message, icon, iconTint, backgroundColor, durationMs) {
        ToastData(actualMessage, actualIcon, iconTint, backgroundColor, durationMs)
    }
}

@Composable
fun rememberSuccessToast(
    @StringRes message: Int,
    durationMs: Long = DEFAULT_DURATION_MS,
): ToastData {
    return rememberToast(
        message = message,
        icon = CommonDrawable.ic_success,
        iconTint = LocalCuiPalette.current.IconPositive,
        backgroundColor = LocalCuiPalette.current.BackgroundPositiveSecondary,
        durationMs = durationMs,
    )
}

@Composable
fun rememberWarningToast(
    @StringRes message: Int,
    durationMs: Long = DEFAULT_DURATION_MS,
): ToastData {
    return rememberToast(
        message = message,
        icon = CommonDrawable.ic_warning,
        iconTint = LocalCuiPalette.current.IconWarning,
        backgroundColor = LocalCuiPalette.current.BackgroundWarningSecondary,
        durationMs = durationMs,
    )
}


@Composable
fun rememberErrorToast(
    @StringRes message: Int,
    durationMs: Long = DEFAULT_DURATION_MS,
): ToastData {
    return rememberToast(
        message = message,
        icon = CommonDrawable.ic_error,
        iconTint = LocalCuiPalette.current.IconNegative,
        backgroundColor = LocalCuiPalette.current.BackgroundNegativeSecondary,
        durationMs = durationMs,
    )
}

@Stable
data class ToastData(
    val message: String,
    val icon: Painter?,
    val iconTint: Color,
    val backgroundColor: Color,
    val durationMs: Long,
)

@Composable
fun ToastHost(
    controller: ToastController = LocalToastController.current,
    toast: @Composable (ToastData) -> Unit = { data ->
        CuiToast(
            data = data,
            onClick = controller::dismiss,
            onSwipeOut = controller::dismiss,
        )
    },
) {
    val currentToastData = controller.currentToastData

    LaunchedEffect(controller.show) {
        if (currentToastData != null && controller.show) {
            delay(currentToastData.durationMs)
            controller.dismiss()
        }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = controller.show && currentToastData != null,
            enter = slideInVertically(),
            exit = slideOutVertically(targetOffsetY = { -it }),
        ) {
            toast(currentToastData!!)
        }
    }
}