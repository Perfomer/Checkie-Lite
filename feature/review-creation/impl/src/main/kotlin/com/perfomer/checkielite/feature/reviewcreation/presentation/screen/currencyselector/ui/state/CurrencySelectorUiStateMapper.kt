package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.state

import android.content.Context
import android.icu.util.Currency
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.entity.price.CurrencySymbol
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorState
import kotlinx.collections.immutable.toPersistentList

internal class CurrencySelectorUiStateMapper(
    private val context: Context,
) : UiStateMapper<CurrencySelectorState, CurrencySelectorUiState> {

    override fun map(state: CurrencySelectorState): CurrencySelectorUiState {
        return CurrencySelectorUiState(
            searchQuery = state.searchQuery,
            currencies = state.filteredCurrencies
                .map { it.toUiItem(state.selectedCurrency.currencyCode) }
                .toPersistentList(),
        )
    }

    private companion object {

        private fun Currency.toUiItem(selectedCode: String): CurrencyItem {
            return CurrencyItem(
                code = currencyCode,
                displayName = displayName,
                symbol = CurrencySymbol.getSymbol(currencyCode) ?: symbol,
                isSelected = currencyCode == selectedCode,
            )
        }
    }
}