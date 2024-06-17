package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.actor

import android.icu.util.Currency
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorCommand.LoadCurrencies
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEvent.CurrenciesLoading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
internal class LoadCurrenciesActor(
    private val localDataSource: CheckieLocalDataSource,
) : Actor<CurrencySelectorCommand, CurrencySelectorEvent> {

    override fun act(commands: Flow<CurrencySelectorCommand>): Flow<CurrencySelectorEvent> {
        return commands.filterIsInstance<LoadCurrencies>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadCurrencies): Flow<CurrencySelectorEvent> {
        return flowBy { localDataSource.getAllCurrenciesCodes() }
            .map { currencies -> currencies.map(Currency::getInstance) }
            .map(CurrenciesLoading::Succeed)
            .onCatchReturn(CurrenciesLoading::Failed)
            .startWith(CurrenciesLoading.Started)
    }
}