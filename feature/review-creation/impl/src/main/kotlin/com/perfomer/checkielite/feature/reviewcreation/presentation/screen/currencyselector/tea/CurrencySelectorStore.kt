package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea

import android.icu.util.Currency
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ScreenModelStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.CurrencySelectorParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.state.CurrencySelectorUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.state.CurrencySelectorUiStateMapper

internal class CurrencySelectorStore(
    params: CurrencySelectorParams,
    reducer: CurrencySelectorReducer,
    uiStateMapper: CurrencySelectorUiStateMapper,
    actors: Set<Actor<CurrencySelectorCommand, CurrencySelectorEvent>>,
) : ScreenModelStore<CurrencySelectorCommand, CurrencySelectorEffect, CurrencySelectorEvent, CurrencySelectorUiEvent, CurrencySelectorState, CurrencySelectorUiState>(
    initialState = CurrencySelectorState(selectedCurrency = Currency.getInstance(params.currentCurrency.code)),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialEvents = listOf(Initialize),
)