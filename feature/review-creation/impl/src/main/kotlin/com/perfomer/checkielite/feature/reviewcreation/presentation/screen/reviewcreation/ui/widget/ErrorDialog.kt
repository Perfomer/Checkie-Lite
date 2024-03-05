package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.feature.reviewcreation.R

@Composable
internal fun ErrorDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(CommonString.common_error_title)) },
        text = { Text(stringResource(CommonString.common_error_message)) },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(R.string.reviewcreation_dialog_confirmexit_confirm),
                )
            }
        },
        tonalElevation = 0.dp,
    )
}