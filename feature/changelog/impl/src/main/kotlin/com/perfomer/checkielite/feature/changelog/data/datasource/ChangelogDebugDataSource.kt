package com.perfomer.checkielite.feature.changelog.data.datasource

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

internal interface ChangelogDebugDataSource {

    suspend fun loadChangelog(language: String): String
}

internal class ChangelogDebugDataSourceImpl(
    private val context: Context,
) : ChangelogDebugDataSource {

    override suspend fun loadChangelog(language: String): String = withContext(Dispatchers.IO) {
        delay(2000)

        return@withContext context.assets.open(CHANGELOG_ASSET_TEMPLATE.format(language.uppercase()))
            .bufferedReader()
            .use { it.readText() }
    }

    private companion object {
        private const val CHANGELOG_ASSET_TEMPLATE = "changelog-%s.md"
    }
}