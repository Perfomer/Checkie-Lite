package com.perfomer.checkielite.core.data.datasource.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.perfomer.checkielite.core.domain.entity.price.CheckieCurrency
import com.perfomer.checkielite.core.domain.entity.sort.TagSortingStrategy
import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal interface PreferencesDataSource {

    suspend fun getLatestCurrency(): CheckieCurrency?

    suspend fun setLatestCurrency(currency: CheckieCurrency)

    suspend fun getLatestTagSortingStrategy(): TagSortingStrategy?

    suspend fun setLatestTagSortingStrategy(strategy: TagSortingStrategy)

    suspend fun getThemeMode(): ThemeMode?

    suspend fun setThemeMode(themeMode: ThemeMode)
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

    override suspend fun getLatestTagSortingStrategy(): TagSortingStrategy?  = withContext(Dispatchers.IO) {
        val value = preferences.getString(KEY_LATEST_TAG_SORT, null) ?: return@withContext null
        return@withContext TagSortingStrategy.valueOf(value)
    }

    override suspend fun setLatestTagSortingStrategy(strategy: TagSortingStrategy)  = withContext(Dispatchers.IO) {
        preferences.edit()
            .putString(KEY_LATEST_TAG_SORT, strategy.name)
            .commit()

        Unit
    }

    override suspend fun getThemeMode(): ThemeMode? = withContext(Dispatchers.IO) {
        val value = preferences.getString(KEY_THEME_MODE, null) ?: return@withContext null

        return@withContext ThemeMode.valueOf(value)
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) = withContext(Dispatchers.IO) {
        preferences.edit()
            .putString(KEY_THEME_MODE, themeMode.name)
            .commit()

        Unit
    }

    private companion object {

        private const val PREF_NAME = "checkielite"

        private const val KEY_LATEST_CURRENCY = "latest_currency"
        private const val KEY_LATEST_TAG_SORT = "latest_tag_sort"
        private const val KEY_THEME_MODE = "theme_mode"
    }
}