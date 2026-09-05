package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core

import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastStyle
import com.perfomer.checkielite.common.ui.util.resource.text.Text

internal sealed interface ReviewDetailsEffect {

    data object ShowConfirmDeleteDialog : ReviewDetailsEffect

    class ShowToast(
        val text: Text,
        val style: ToastStyle,
    ) : ReviewDetailsEffect
}