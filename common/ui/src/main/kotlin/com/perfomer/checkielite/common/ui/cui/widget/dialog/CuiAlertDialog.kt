package com.perfomer.checkielite.common.ui.cui.widget.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

@Composable
fun CuiAlertDialog(
    title: String,
    text: String,
    confirmText: String = stringResource(CommonString.common_ok),
    confirmColor: Color = LocalCuiPalette.current.TextAccent,
    onConfirm: () -> Unit,
    dismissText: String? = stringResource(CommonString.common_cancel),
    dismissColor: Color = LocalCuiPalette.current.TextAccent,
    onDismiss: () -> Unit,
) {
    CuiAlertDialogInternal(
        title = title,
        text = { Text(text) },
        confirmText = confirmText,
        confirmColor = confirmColor,
        onConfirm = onConfirm,
        dismissText = dismissText,
        dismissColor = dismissColor,
        onDismiss = onDismiss,
    )
}

@Composable
fun CuiAlertDialog(
    title: String,
    text: AnnotatedString,
    confirmText: String = stringResource(CommonString.common_ok),
    confirmColor: Color = LocalCuiPalette.current.TextAccent,
    onConfirm: () -> Unit,
    dismissText: String? = stringResource(CommonString.common_cancel),
    dismissColor: Color = LocalCuiPalette.current.TextAccent,
    onDismiss: () -> Unit,
) {
    CuiAlertDialogInternal(
        title = title,
        text = { Text(text) },
        confirmText = confirmText,
        confirmColor = confirmColor,
        onConfirm = onConfirm,
        dismissText = dismissText,
        dismissColor = dismissColor,
        onDismiss = onDismiss,
    )
}

@Composable
private fun CuiAlertDialogInternal(
    title: String,
    text: @Composable () -> Unit,
    confirmText: String,
    confirmColor: Color,
    onConfirm: () -> Unit,
    dismissText: String?,
    dismissColor: Color,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = title,
                color = LocalCuiPalette.current.TextPrimary,
            )
        },
        text = {
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.copy(color = LocalCuiPalette.current.TextPrimary),
                content = text,
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(text = confirmText, color = confirmColor)
            }
        },
        dismissButton = dismissText?.let {
            {
                TextButton(onClick = onDismiss) {
                    Text(text = dismissText, color = dismissColor)
                }
            }
        },
        containerColor = LocalCuiPalette.current.BackgroundElevationBase,
        tonalElevation = 0.dp,
    )
}