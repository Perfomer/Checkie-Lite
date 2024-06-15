package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.widget

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.feature.reviewcreation.R

@Composable
internal fun ConfirmDeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(R.string.tagcreation_dialog_confirmdelete_title)) },
        text = { Text(stringResource(R.string.tagcreation_dialog_confirmdelete_message)) },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(R.string.tagcreation_dialog_confirmdelete_confirm),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(CommonString.common_cancel))
            }
        },
        tonalElevation = 0.dp,
    )
}