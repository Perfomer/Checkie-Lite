package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.CurrencySelectorParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.CurrencySelectorStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent.OnCurrencyClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent.OnSearchQueryClearClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent.OnSearchQueryInput
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class CurrencySelectorContentScreen(
    private val params: CurrencySelectorParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<CurrencySelectorStore>(params)) { state ->
        BackHandlerWithLifecycle { accept(OnBackPress) }

        CurrencySelectorScreen(
            state = state,

            onSearchQueryInput = acceptable(::OnSearchQueryInput),
            onSearchQueryClearClick = acceptable(OnSearchQueryClearClick),
            onCurrencyClick = acceptable(::OnCurrencyClick),
            onDoneClick = acceptable(OnDoneClick),
        )
    }
}