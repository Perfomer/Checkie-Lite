package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core

import android.icu.util.Currency
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.CurrencySelectorResult

internal sealed interface CurrencySelectorCommand {

    data object LoadCurrencies : CurrencySelectorCommand

    class FilterCurrencies(
        val query: String,
        val currencies: List<Currency>,
    ) : CurrencySelectorCommand
}

internal sealed interface CurrencySelectorNavigationCommand : CurrencySelectorCommand {

    data object Exit : CurrencySelectorNavigationCommand

    class ExitWithResult(val result: CurrencySelectorResult) : CurrencySelectorNavigationCommand
}