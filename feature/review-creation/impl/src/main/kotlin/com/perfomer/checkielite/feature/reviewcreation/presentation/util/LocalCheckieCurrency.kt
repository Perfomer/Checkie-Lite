package com.perfomer.checkielite.feature.reviewcreation.presentation.util

import com.perfomer.checkielite.core.entity.price.CheckieCurrency
import java.util.Date
import java.util.Locale
import android.icu.util.Currency as AndroidCurrency

internal object LocalCheckieCurrency {

    private const val DEFAULT_CURRENCY_CODE = "USD"

    fun getLocalCurrency(default: String = DEFAULT_CURRENCY_CODE): CheckieCurrency {
        return CheckieCurrency(getLocalCurrencyCode(default))
    }

    fun getLocalCurrencyCode(default: String = DEFAULT_CURRENCY_CODE): String {
        val localCurrencyCode = AndroidCurrency.getAvailableCurrencyCodes(Locale.getDefault(), Date()).firstOrNull()
        return localCurrencyCode ?: default
    }
}