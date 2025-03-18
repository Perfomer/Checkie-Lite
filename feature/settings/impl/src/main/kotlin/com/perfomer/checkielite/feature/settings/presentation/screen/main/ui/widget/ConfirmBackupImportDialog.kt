package com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.widget

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.widget.dialog.CuiAlertDialog
import com.perfomer.checkielite.common.ui.util.span.annotatedStringResource
import com.perfomer.checkielite.feature.settings.R

@Composable
internal fun ConfirmBackupImportDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    CuiAlertDialog(
        isVisible = isVisible,
        title = stringResource(CommonString.common_warning),
        text = annotatedStringResource(R.string.settings_backup_dialog_confirmimport_message),
        confirmText = stringResource(CommonString.common_proceed),
        confirmColor = MaterialTheme.colorScheme.error,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}