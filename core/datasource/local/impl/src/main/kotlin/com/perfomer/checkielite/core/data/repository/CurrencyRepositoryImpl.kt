package com.perfomer.checkielite.core.data.repository

import android.icu.util.Currency
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.preferences.PreferencesDataSource
import com.perfomer.checkielite.core.domain.entity.price.CheckieCurrency
import com.perfomer.checkielite.core.domain.entity.price.CurrencySymbol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Locale

internal class CurrencyRepositoryImpl(
    private val databaseDataSource: DatabaseDataSource,
    private val preferencesDataSource: PreferencesDataSource,
) : CurrencyRepository {

    @Volatile
    private var allCurrenciesCodes: List<String>? = null
    private val mutex = Mutex()

    override suspend fun getAllCurrenciesCodes(): List<String> = mutex.withLock {
        val now = Date()
        val usedCurrencies = databaseDataSource.getUsedCurrencies().map { it.code }
        val localCurrencies = Currency.getAvailableCurrencyCodes(Locale.getDefault(), now)

        if (allCurrenciesCodes == null) {
            allCurrenciesCodes = withContext(Dispatchers.IO) {
                Locale.getAvailableLocales()
                    .asSequence()
                    .flatMap { locale -> Currency.getAvailableCurrencyCodes(locale, now).orEmpty().toList() }
                    .distinct()
                    .map(Currency::getInstance)
                    .sortedWith(
                        compareBy(
                            { it.currencyCode !in MOST_COMMON_CURRENCY_CODES },
                            { (CurrencySymbol.getSymbol(it.currencyCode) ?: it.symbol).length > 1 },
                            { it.displayName },
                        )
                    )
                    .map(Currency::getCurrencyCode)
                    .toList()
            }
        }

        return (usedCurrencies + localCurrencies + allCurrenciesCodes.orEmpty())
            .distinct()
    }

    override suspend fun getLatestCurrency(): CheckieCurrency? {
        return preferencesDataSource.getLatestCurrency()
    }

    private companion object {
        private val MOST_COMMON_CURRENCY_CODES = listOf("USD", "EUR")
    }
}