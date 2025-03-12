package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.content
import com.perfomer.checkielite.common.pure.state.toLoading
import com.perfomer.checkielite.common.pure.util.remove
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.core.domain.entity.price.CheckieCurrency
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.CurrencySelectorResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorCommand.FilterCurrencies
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorCommand.LoadCurrencies
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEvent.CurrenciesLoading
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEvent.OnCurrenciesFiltered
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
        is OnCurrenciesFiltered -> reduceOnCurrenciesFiltered(event)
    }

    private fun reduceInitialize() {
        commands(LoadCurrencies)
    }

    private fun reduceUi(event: CurrencySelectorUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
        is OnDoneClick -> reduceOnDoneClick()
        is OnCurrencyClick -> {
            val currency = state.allCurrencies.content?.find { it.currencyCode == event.code }
            if (currency != null) state { copy(selectedCurrency = currency) }
            else Unit
        }
        is OnSearchQueryClearClick -> updateSearchQuery("")
        is OnSearchQueryInput -> updateSearchQuery(event.query)
    }

    private fun reduceTagLoading(event: CurrenciesLoading) = when (event) {
        is CurrenciesLoading.Started -> state { copy(allCurrencies = allCurrencies.toLoading()) }
        is CurrenciesLoading.Succeed -> {
            state {
                copy(
                    allCurrencies = Lce.Content(
                        buildList {
                            add(selectedCurrency)
                            addAll(event.currencies.remove(selectedCurrency))
                        })
                )
            }

            updateSearchQuery()
        }
        is CurrenciesLoading.Failed -> commands(Exit)
    }

    private fun reduceOnCurrenciesFiltered(event: OnCurrenciesFiltered) {
        state { copy(filteredCurrencies = event.currencies) }
    }

    private fun reduceOnDoneClick() {
        val selectedCurrency = state.selectedCurrency
        val result = CurrencySelectorResult.Selected(
            currency = CheckieCurrency(
                code = selectedCurrency.currencyCode,
                symbol = selectedCurrency.symbol,
            ),
        )
        commands(ExitWithResult(result))
    }

    private fun updateSearchQuery(query: String = state.searchQuery) {
        state { copy(searchQuery = query) }
        commands(FilterCurrencies(query, state.allCurrencies.content.orEmpty()))
    }
}