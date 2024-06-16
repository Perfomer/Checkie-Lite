package com.perfomer.checkielite.feature.reviewcreation.presentation.navigation

import com.perfomer.checkielite.core.entity.price.CheckieCurrency
import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params

internal class CurrencySelectorParams(val currentCurrency: CheckieCurrency) : Params

internal sealed interface CurrencySelectorResult {

    data class Selected(val currency: CheckieCurrency) : CurrencySelectorResult
}

internal fun interface CurrencySelectorScreenProvider {

    operator fun invoke(params: CurrencySelectorParams): CheckieScreen
}