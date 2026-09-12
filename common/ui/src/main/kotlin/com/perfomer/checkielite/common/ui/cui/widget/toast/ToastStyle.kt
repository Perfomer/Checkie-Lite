package com.perfomer.checkielite.common.ui.cui.widget.toast

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.util.resource.image.Image

enum class ToastStyle {
    NEUTRAL,
    SUCCESS,
    WARNING,
    ERROR
}

@get:Composable
internal val ToastStyle.backgroundColor: Color
    get() = when (this) {
        ToastStyle.NEUTRAL -> LocalCuiPalette.current.BackgroundElevationContent
        ToastStyle.SUCCESS -> LocalCuiPalette.current.BackgroundPositiveSecondary
        ToastStyle.WARNING -> LocalCuiPalette.current.BackgroundWarningSecondary
        ToastStyle.ERROR -> LocalCuiPalette.current.BackgroundNegativeSecondary
    }

@get:Composable
internal val ToastStyle.iconTint: Color
    get() = when (this) {
        ToastStyle.NEUTRAL -> Color.Unspecified
        ToastStyle.SUCCESS -> LocalCuiPalette.current.IconPositive
        ToastStyle.WARNING -> LocalCuiPalette.current.IconWarning
        ToastStyle.ERROR -> LocalCuiPalette.current.IconNegative
    }

@get:Composable
internal val ToastStyle.icon: Image?
    get() = when (this) {
        ToastStyle.NEUTRAL -> null
        ToastStyle.SUCCESS -> Image.resource(CommonDrawable.ic_success)
        ToastStyle.WARNING -> Image.resource(CommonDrawable.ic_warning)
        ToastStyle.ERROR -> Image.resource(CommonDrawable.ic_error)
    }