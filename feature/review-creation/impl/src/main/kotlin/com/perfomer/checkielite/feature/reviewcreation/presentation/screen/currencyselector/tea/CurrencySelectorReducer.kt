package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.content
import com.perfomer.checkielite.common.pure.state.toLoading
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.CurrencySelectorResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorCommand.LoadCurrencies
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEvent.CurrenciesLoading
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent.OnCurrencyClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent.OnSearchQueryClearClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent.OnSearchQueryInput

internal class CurrencySelectorReducer : DslReducer<CurrencySelectorCommand, CurrencySelectorEffect, CurrencySelectorEvent, CurrencySelectorState>() {

    override fun reduce(event: CurrencySelectorEvent) = when (event) {
        is Initialize -> reduceInitialize()

        is CurrencySelectorUiEvent -> reduceUi(event)
        is CurrenciesLoading -> reduceTagLoading(event)
    }

    private fun reduceInitialize() {
        commands(LoadCurrencies(""))
    }

    private fun reduceUi(event: CurrencySelectorUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
        is OnDoneClick -> reduceOnDoneClick()
        is OnCurrencyClick -> {
            val currency = state.allCurrencies.content?.find { it.code == event.code }
            if (currency != null) state { copy(selectedCurrency = currency) }
            else Unit
        }
        is OnSearchQueryClearClick -> updateSearchQuery("")
        is OnSearchQueryInput -> updateSearchQuery(event.query)
    }

    private fun reduceTagLoading(event: CurrenciesLoading) = when (event) {
        is CurrenciesLoading.Started -> state { copy(allCurrencies = allCurrencies.toLoading()) }
        is CurrenciesLoading.Succeed -> state { copy(allCurrencies = Lce.Content(event.currencies)) }
        is CurrenciesLoading.Failed -> commands(Exit)
    }

    private fun reduceOnDoneClick() {
        val result = CurrencySelectorResult.Selected(state.selectedCurrency)
        commands(ExitWithResult(result))
    }

    private fun updateSearchQuery(query: String) {
        state { copy(searchQuery = query) }
        commands(LoadCurrencies(query))
    }
}