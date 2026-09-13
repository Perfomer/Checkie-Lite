package com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core

import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastStyle
import com.perfomer.checkielite.common.ui.util.resource.text.Text

internal sealed interface MainEffect {

    class ShowToast(
        val text: Text,
        val style: ToastStyle,
    ) : MainEffect
}