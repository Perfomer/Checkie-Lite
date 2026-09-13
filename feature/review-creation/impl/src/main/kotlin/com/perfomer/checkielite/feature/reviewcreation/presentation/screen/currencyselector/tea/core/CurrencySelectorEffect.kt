package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core

import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastStyle
import com.perfomer.checkielite.common.ui.util.resource.text.Text

internal sealed interface CurrencySelectorEffect {

    data object FocusTagValueField : CurrencySelectorEffect

    class ShowToast(
        val text: Text,
        val style: ToastStyle,
    ) : CurrencySelectorEffect

    data object ShowTagDeleteConfirmationDialog : CurrencySelectorEffect
}