package com.perfomer.checkielite.core.data.datasource

import android.icu.util.Currency
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.preferences.PreferencesDataSource
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.price.CheckieCurrency
import com.perfomer.checkielite.core.entity.price.CurrencySymbol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Locale

internal class CheckieLocalDataSourceImpl(
    private val databaseDataSource: DatabaseDataSource,
    private val preferencesDataSource: PreferencesDataSource,
) : CheckieLocalDataSource {

    @Volatile
    private var allCurrenciesCodes: List<String>? = null
    private val mutex = Mutex()

    override fun getRecentSearches(): Flow<List<CheckieReview>> {
        return databaseDataSource.getRecentSearches()
    }

    override suspend fun rememberRecentSearch(reviewId: String) {
        return databaseDataSource.rememberRecentSearch(
            reviewId = reviewId,
            searchDate = Date(),
        )
    }

    override suspend fun clearRecentSearches() {
        return databaseDataSource.clearRecentSearches()
    }

    override suspend fun dropSyncing() {
        databaseDataSource.dropSyncing()
    }

    override fun isSyncing(): Flow<Boolean> {
        return databaseDataSource.isSyncing()
    }

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