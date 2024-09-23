package com.perfomer.checkielite.common.ui.cui.widget.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.CommonString

@Composable
fun CuiAlertDialog(
    title: String,
    text: String,
    confirmText: String = stringResource(CommonString.common_ok),
    confirmColor: Color = Color.Unspecified,
    onConfirm: () -> Unit,
    dismissText: String? = stringResource(CommonString.common_cancel),
    dismissColor: Color = Color.Unspecified,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(title) },
        text = { Text(text) },
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
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
    )
}