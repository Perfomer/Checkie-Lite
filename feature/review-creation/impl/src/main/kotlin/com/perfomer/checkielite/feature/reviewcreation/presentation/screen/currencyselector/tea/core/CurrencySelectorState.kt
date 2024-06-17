package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core

import android.icu.util.Currency
import com.perfomer.checkielite.common.pure.state.Lce

internal data class CurrencySelectorState(
    val selectedCurrency: Currency,
    val allCurrencies: Lce<List<Currency>> = Lce.initial(),
    val filteredCurrencies: List<Currency> = emptyList(),
    val searchQuery: String = "",
)
