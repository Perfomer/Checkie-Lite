package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.widget.dialog.CuiAlertDialog
import com.perfomer.checkielite.feature.reviewcreation.R

@Composable
internal fun ErrorDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    CuiAlertDialog(
        title = stringResource(CommonString.common_error_title),
        text = stringResource(CommonString.common_error_message),
        confirmText = stringResource(R.string.reviewcreation_dialog_confirmexit_confirm),
        dismissText = null,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}