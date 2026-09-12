package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastStyle
import com.perfomer.checkielite.common.ui.util.resource.text.Text

internal sealed interface BackupEffect {

    class ShowToast(
        val text: Text,
        val style: ToastStyle,
    ) : BackupEffect
}
