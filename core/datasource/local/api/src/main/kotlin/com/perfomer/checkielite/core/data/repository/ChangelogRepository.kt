package com.perfomer.checkielite.core.data.repository

interface ChangelogRepository {

    suspend fun getLastSeenVersionCode(): Int?

    suspend fun setLastSeenVersionCode(versionCode: Int)
}
