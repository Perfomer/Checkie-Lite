package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core

import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.CurrencySelectorResult

internal sealed interface CurrencySelectorCommand {

    class LoadCurrencies(val query: String) : CurrencySelectorCommand
}

internal sealed interface CurrencySelectorNavigationCommand : CurrencySelectorCommand {

    data object Exit : CurrencySelectorNavigationCommand

    class ExitWithResult(val result: CurrencySelectorResult) : CurrencySelectorNavigationCommand
}