package com.perfomer.checkielite.feature.changelog.domain.repository

internal interface ChangelogContentRepository {

    suspend fun loadChangelog(): String
}