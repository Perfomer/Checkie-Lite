package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.state

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class CurrencySelectorUiState(
    val searchQuery: String,
    val currencies: ImmutableList<CurrencyItem>,
)

@Immutable
internal data class CurrencyItem(
    val code: String,
    val displayName: String,
    val symbol: String,
    val isSelected: Boolean,
)