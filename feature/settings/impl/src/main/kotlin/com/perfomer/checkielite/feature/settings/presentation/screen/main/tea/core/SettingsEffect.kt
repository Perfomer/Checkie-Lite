package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastStyle
import com.perfomer.checkielite.common.ui.util.resource.text.Text

internal sealed interface SettingsEffect {

    class ShowToast(
        val text: Text,
        val style: ToastStyle,
    ) : SettingsEffect

    data object ShowConfirmImportDialog : SettingsEffect
}
