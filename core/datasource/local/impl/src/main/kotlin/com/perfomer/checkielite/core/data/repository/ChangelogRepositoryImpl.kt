package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.core.data.datasource.preferences.PreferencesDataSource

internal class ChangelogRepositoryImpl(
    private val preferencesDataSource: PreferencesDataSource,
) : ChangelogRepository {

    override suspend fun getLastSeenVersionCode(): Int? {
        return preferencesDataSource.getLastSeenChangelogVersionCode()
    }

    override suspend fun setLastSeenVersionCode(versionCode: Int) {
        preferencesDataSource.setLastSeenChangelogVersionCode(versionCode)
    }
}
