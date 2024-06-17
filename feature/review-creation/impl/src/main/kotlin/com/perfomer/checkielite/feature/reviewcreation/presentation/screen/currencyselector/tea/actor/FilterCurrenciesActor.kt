package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.actor

import com.perfomer.checkielite.common.pure.search.smartFilterByQuery
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorCommand.FilterCurrencies
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEvent.OnCurrenciesFiltered
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
internal class FilterCurrenciesActor : Actor<CurrencySelectorCommand, CurrencySelectorEvent> {

    override fun act(commands: Flow<CurrencySelectorCommand>): Flow<CurrencySelectorEvent> {
        return commands.filterIsInstance<FilterCurrencies>()
            .mapLatest(::handleCommand)
    }

    private suspend fun handleCommand(command: FilterCurrencies): CurrencySelectorEvent {
        return withContext(Dispatchers.IO) {
            val filteredCurrencies = command.currencies.smartFilterByQuery(
                query = command.query,
                fieldProvider = {
                    listOf(
                        displayName to 1F,
                        currencyCode to 1F,
                        symbol to 1F,
                    )
                },
            )

            return@withContext OnCurrenciesFiltered(filteredCurrencies)
        }
    }
}