package com.perfomer.checkielite.feature.reviewcreation.presentation.navigation

import com.perfomer.checkielite.core.domain.entity.price.CheckieCurrency
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.Result
import kotlinx.serialization.Serializable

@Serializable
internal data class CurrencySelectorDestination(
    val currentCurrency: CheckieCurrency,
) : Destination()

internal sealed interface CurrencySelectorResult : Result {
    data class Selected(val currency: CheckieCurrency) : CurrencySelectorResult
}