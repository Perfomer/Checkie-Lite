package com.perfomer.checkielite.feature.changelog.data.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class ChangelogRemoteDataSourceImpl(
    private val client: HttpClient,
) : ChangelogRemoteDataSource {

    override suspend fun loadChangelog(language: String): String {
        val url = CHANGELOG_URL_TEMPLATE.format(language.uppercase())
        return client.get(url).body<String>()
    }

    private companion object {
        private const val CHANGELOG_URL_TEMPLATE = "https://raw.githubusercontent.com/Perfomer/Checkie-Lite/master/changelog/changelog-%s.md"
    }
}
