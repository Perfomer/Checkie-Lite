package com.perfomer.checkielite.core.data.datasource.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.perfomer.checkielite.core.entity.price.CheckieCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal interface PreferencesDataSource {

    suspend fun getLatestCurrency(): CheckieCurrency?

    suspend fun setLatestCurrency(currency: CheckieCurrency)
}

@SuppressLint("ApplySharedPref")
internal class PreferencesDataSourceImpl(
    context: Context,
) : PreferencesDataSource {

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    override suspend fun getLatestCurrency(): CheckieCurrency? = withContext(Dispatchers.IO) {
        val currencyCode = preferences.getString(KEY_LATEST_CURRENCY, null) ?: return@withContext null
        return@withContext CheckieCurrency(currencyCode)
    }

    override suspend fun setLatestCurrency(currency: CheckieCurrency) = withContext(Dispatchers.IO) {
        preferences.edit()
            .putString(KEY_LATEST_CURRENCY, currency.code)
            .commit()

        Unit
    }

    private companion object {

        private const val PREF_NAME = "checkielite"

        private const val KEY_LATEST_CURRENCY = "latest_currency"
    }
}