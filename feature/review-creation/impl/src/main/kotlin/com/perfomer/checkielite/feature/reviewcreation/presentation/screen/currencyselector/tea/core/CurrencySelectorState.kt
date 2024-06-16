package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.core.entity.price.CheckieCurrency

internal data class CurrencySelectorState(
    val selectedCurrency: CheckieCurrency,
    val allCurrencies: Lce<List<CheckieCurrency>> = Lce.initial(),
    val searchQuery: String = "",
)
