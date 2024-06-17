package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core

import android.icu.util.Currency

internal sealed interface CurrencySelectorEvent {

    data object Initialize : CurrencySelectorEvent

    sealed interface CurrenciesLoading : CurrencySelectorEvent {
        data object Started : CurrenciesLoading
        class Succeed(val currencies: List<Currency>) : CurrenciesLoading
        class Failed(val throwable: Throwable) : CurrenciesLoading
    }

    class OnCurrenciesFiltered(val currencies: List<Currency>) : CurrencySelectorEvent
}

internal sealed interface CurrencySelectorUiEvent : CurrencySelectorEvent {

    data object OnBackPress : CurrencySelectorUiEvent

    class OnSearchQueryInput(val query: String) : CurrencySelectorUiEvent

    data object OnSearchQueryClearClick : CurrencySelectorUiEvent

    class OnCurrencyClick(val code: String) : CurrencySelectorUiEvent

    data object OnDoneClick : CurrencySelectorUiEvent
}

internal sealed interface CurrencySelectorNavigationEvent : CurrencySelectorEvent