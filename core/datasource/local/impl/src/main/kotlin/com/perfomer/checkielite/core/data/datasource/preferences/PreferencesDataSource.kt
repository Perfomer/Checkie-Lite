package com.perfomer.checkielite.core.data.datasource.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.perfomer.checkielite.core.domain.entity.price.CheckieCurrency
import com.perfomer.checkielite.core.domain.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.core.domain.entity.sort.TagSortingStrategy
import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal interface PreferencesDataSource {

    suspend fun getLatestCurrency(): CheckieCurrency?
    suspend fun setLatestCurrency(currency: CheckieCurrency)

    suspend fun getLatestTagSortingStrategy(): TagSortingStrategy?
    suspend fun setLatestTagSortingStrategy(strategy: TagSortingStrategy)

    suspend fun getLatestTagSearchSortingStrategy(): ReviewsSortingStrategy
    suspend fun setLatestTagSearchSortingStrategy(strategy: ReviewsSortingStrategy)

    suspend fun getThemeMode(): ThemeMode?
    suspend fun setThemeMode(themeMode: ThemeMode)

    suspend fun isLiquidGlassEnabled(): Boolean
    suspend fun setLiquidGlassEnabled(enabled: Boolean)

    suspend fun getLastSeenChangelogVersionCode(): Int?
    suspend fun setLastSeenChangelogVersionCode(versionCode: Int)
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
        preferences.edit(commit = true) {
            putString(KEY_LATEST_CURRENCY, currency.code)
        }
    }

    override suspend fun getLatestTagSortingStrategy(): TagSortingStrategy? = withContext(Dispatchers.IO) {
        val value = preferences.getString(KEY_LATEST_TAG_SORT, null) ?: return@withContext null
        return@withContext TagSortingStrategy.valueOf(value)
    }

    override suspend fun setLatestTagSortingStrategy(strategy: TagSortingStrategy) = withContext(Dispatchers.IO) {
        preferences.edit(commit = true) {
            putString(KEY_LATEST_TAG_SORT, strategy.name)
        }
    }

    override suspend fun getLatestTagSearchSortingStrategy(): ReviewsSortingStrategy = withContext(Dispatchers.IO) {
        val value = preferences.getString(KEY_LATEST_TAG_SEARCH_SORT, null) ?: return@withContext ReviewsSortingStrategy.MOST_RATED
        return@withContext ReviewsSortingStrategy.valueOf(value)
    }

    override suspend fun setLatestTagSearchSortingStrategy(strategy: ReviewsSortingStrategy) = withContext(Dispatchers.IO) {
        preferences.edit(commit = true) {
            putString(KEY_LATEST_TAG_SEARCH_SORT, strategy.name)
        }
    }

    override suspend fun getThemeMode(): ThemeMode? = withContext(Dispatchers.IO) {
        val value = preferences.getString(KEY_THEME_MODE, null) ?: return@withContext null

        return@withContext ThemeMode.valueOf(value)
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) = withContext(Dispatchers.IO) {
        preferences.edit(commit = true) {
            putString(KEY_THEME_MODE, themeMode.name)
        }
    }

    override suspend fun getLastSeenChangelogVersionCode(): Int? = withContext(Dispatchers.IO) {
        val value = preferences.getInt(KEY_LAST_SEEN_CHANGELOG_VERSION_CODE, 0)
        return@withContext value.takeIf { it != 0 }
    }

    override suspend fun isLiquidGlassEnabled(): Boolean = withContext(Dispatchers.IO) {
        preferences.getBoolean(KEY_LIQUID_GLASS_ENABLED, true)
    }

    override suspend fun setLiquidGlassEnabled(enabled: Boolean) = withContext(Dispatchers.IO) {
        check(preferences.edit().putBoolean(KEY_LIQUID_GLASS_ENABLED, enabled).commit()) {
            "Failed to save Liquid Glass preference"
        }
    }

    override suspend fun setLastSeenChangelogVersionCode(versionCode: Int) = withContext(Dispatchers.IO) {
        preferences.edit(commit = true) {
            putInt(KEY_LAST_SEEN_CHANGELOG_VERSION_CODE, versionCode)
        }
    }

    private companion object {

        private const val PREF_NAME = "checkielite"

        private const val KEY_LATEST_CURRENCY = "latest_currency"
        private const val KEY_LATEST_TAG_SORT = "latest_tag_sort"
        private const val KEY_LATEST_TAG_SEARCH_SORT = "latest_tag_search_sort"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_LIQUID_GLASS_ENABLED = "liquid_glass_enabled"
        private const val KEY_LAST_SEEN_CHANGELOG_VERSION_CODE = "last_seen_changelog_version_code"
    }
}
