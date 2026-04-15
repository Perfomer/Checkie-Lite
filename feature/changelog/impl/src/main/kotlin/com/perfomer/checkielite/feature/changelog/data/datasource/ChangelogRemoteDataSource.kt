package com.perfomer.checkielite.feature.changelog.data.datasource

internal interface ChangelogRemoteDataSource {

    suspend fun loadChangelog(language: String): String
}
