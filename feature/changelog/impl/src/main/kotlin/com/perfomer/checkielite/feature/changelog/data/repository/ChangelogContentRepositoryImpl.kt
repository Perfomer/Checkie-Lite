package com.perfomer.checkielite.feature.changelog.data.repository

import android.content.Context
import com.perfomer.checkielite.common.android.util.currentLocale
import com.perfomer.checkielite.feature.changelog.data.datasource.ChangelogRemoteDataSource
import com.perfomer.checkielite.feature.changelog.domain.repository.ChangelogContentRepository

internal class ChangelogContentRepositoryImpl(
    private val context: Context,
    private val remoteDataSource: ChangelogRemoteDataSource,
) : ChangelogContentRepository {

    override suspend fun loadChangelog(): String {
        val appLanguage = context.currentLocale.language
        val targetLanguage = appLanguage
            .takeIf(supportedLanguages::contains)
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
