package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea

import android.icu.util.Currency
import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ComponentStore
import com.perfomer.checkielite.common.ui.util.tea.LogUnhandledExceptionHandler
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.CurrencySelectorDestination
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.state.CurrencySelectorUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.state.CurrencySelectorUiStateMapper

internal class CurrencySelectorStore(
    componentContext: ComponentContext,
    destination: CurrencySelectorDestination,
    reducer: CurrencySelectorReducer,
    uiStateMapper: CurrencySelectorUiStateMapper,
    actors: Set<Actor<CurrencySelectorCommand, CurrencySelectorEvent>>,
) : ComponentStore<CurrencySelectorCommand, CurrencySelectorEffect, CurrencySelectorEvent, CurrencySelectorUiEvent, CurrencySelectorState, CurrencySelectorUiState>(
    componentContext = componentContext,
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialState = CurrencySelectorState(
        selectedCurrency = Currency.getInstance(destination.currentCurrency.code),
    ),
    initialEvents = listOf(Initialize),
    unhandledExceptionHandler = LogUnhandledExceptionHandler("CurrencySelectorStore"),
)