package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.perfomer.checkielite.common.ui.cui.widget.dialog.CuiAlertDialog
import com.perfomer.checkielite.feature.reviewcreation.R

@Composable
internal fun ConfirmExitDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    CuiAlertDialog(
        title = stringResource(R.string.reviewcreation_dialog_confirmexit_title),
        text = stringResource(R.string.reviewcreation_dialog_confirmexit_message),
        confirmText = stringResource(R.string.reviewcreation_dialog_confirmexit_confirm),
        confirmColor = MaterialTheme.colorScheme.error,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}