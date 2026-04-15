package com.perfomer.checkielite.feature.changelog.data.repository

import android.content.Context
import androidx.core.os.ConfigurationCompat
import com.perfomer.checkielite.feature.changelog.data.datasource.ChangelogRemoteDataSource
import com.perfomer.checkielite.feature.changelog.domain.repository.ChangelogContentRepository

internal class ChangelogContentRepositoryImpl(
    private val context: Context,
    private val remoteDataSource: ChangelogRemoteDataSource,
) : ChangelogContentRepository {

    override suspend fun loadChangelog(): String {
        val appLanguage = ConfigurationCompat.getLocales(context.resources.configuration)[0]?.language
        val targetLanguage = appLanguage
            ?.takeIf(supportedLanguages::contains)
            ?: DEFAULT_LANGUAGE

        return remoteDataSource.loadChangelog(language = targetLanguage)
    }

    private companion object {

        private const val LANG_RU = "ru"
        private const val LANG_EN = "en"

        private const val DEFAULT_LANGUAGE = LANG_EN

        private val supportedLanguages: List<String> = listOf(
            LANG_RU,
            LANG_EN,
        )
    }
}
