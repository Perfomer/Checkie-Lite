package com.perfomer.checkielite.common.ui.cui.widget.toast

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastData.Companion.DEFAULT_DURATION_MS
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

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