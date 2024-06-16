package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.state

import android.content.Context
import android.icu.util.Currency
import com.perfomer.checkielite.common.pure.state.content
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.entity.price.CheckieCurrency
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core.CurrencySelectorState
import kotlinx.collections.immutable.toPersistentList

internal class CurrencySelectorUiStateMapper(
    private val context: Context,
) : UiStateMapper<CurrencySelectorState, CurrencySelectorUiState> {

    override fun map(state: CurrencySelectorState): CurrencySelectorUiState {
        return CurrencySelectorUiState(
            searchQuery = state.searchQuery,
            currencies = state.allCurrencies.content.orEmpty()
                .map { it.toUiItem(state.selectedCurrency.code) }
                .toPersistentList(),
        )
    }

    private companion object {

        private fun CheckieCurrency.toUiItem(selectedCode: String): CurrencyItem {
            return CurrencyItem(
                code = code,
                displayName = Currency.getInstance(code).displayName,
                symbol = symbol,
                isSelected = code == selectedCode,
            )
        }
    }
}