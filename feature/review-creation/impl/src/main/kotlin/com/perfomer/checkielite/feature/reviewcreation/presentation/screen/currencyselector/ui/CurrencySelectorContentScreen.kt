package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.CurrencySelectorStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent.OnCurrencyClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent.OnSearchQueryClearClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent.OnSearchQueryInput

internal class CurrencySelectorContentScreen(
    private val store: CurrencySelectorStore,
) : Screen {

    @Composable
    override fun Screen() = TeaComposable(store) { state ->
        CurrencySelectorScreen(
            state = state,

            onSearchQueryInput = acceptable(::OnSearchQueryInput),
            onSearchQueryClearClick = acceptable(OnSearchQueryClearClick),
            onCurrencyClick = acceptable(::OnCurrencyClick),
            onDoneClick = acceptable(OnDoneClick),
        )
    }
}