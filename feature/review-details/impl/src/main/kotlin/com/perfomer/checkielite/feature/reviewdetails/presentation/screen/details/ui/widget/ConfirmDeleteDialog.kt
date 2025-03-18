package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.perfomer.checkielite.common.ui.cui.widget.dialog.CuiAlertDialog
import com.perfomer.checkielite.feature.reviewdetails.R

@Composable
internal fun ConfirmDeleteDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    CuiAlertDialog(
        isVisible = isVisible,
        title = stringResource(R.string.reviewdetails_dialog_confirmdelete_title),
        text = stringResource(R.string.reviewdetails_dialog_confirmdelete_message),
        confirmText = stringResource(R.string.reviewdetails_dialog_confirmdelete_confirm),
        confirmColor = MaterialTheme.colorScheme.error,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}