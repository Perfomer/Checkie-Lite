package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.widget

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.perfomer.checkielite.common.ui.cui.widget.dialog.CuiAlertDialog
import com.perfomer.checkielite.feature.reviewcreation.R

@Composable
internal fun ConfirmDeleteDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    CuiAlertDialog(
        isVisible = isVisible,
        title = stringResource(R.string.tagcreation_dialog_confirmdelete_title),
        text = stringResource(R.string.tagcreation_dialog_confirmdelete_message),
        confirmText = stringResource(R.string.tagcreation_dialog_confirmdelete_confirm),
        confirmColor = MaterialTheme.colorScheme.error,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}