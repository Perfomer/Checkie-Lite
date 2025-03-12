package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.common.pure.util.randomUuid
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.preferences.PreferencesDataSource
import com.perfomer.checkielite.core.domain.entity.review.CheckieTag
import com.perfomer.checkielite.core.domain.entity.sort.TagSortingStrategy
import kotlinx.coroutines.flow.Flow

internal class TagRepositoryImpl(
    private val databaseDataSource: DatabaseDataSource,
    private val preferencesDataSource: PreferencesDataSource,
) : TagRepository {

    override fun getTags(searchQuery: String, maxCount: Int): Flow<List<CheckieTag>> {
        return databaseDataSource.getTags(searchQuery, maxCount)
    }

    override suspend fun getTag(id: String): CheckieTag {
        return databaseDataSource.getTag(id)
    }

    override suspend fun getTagByName(name: String): CheckieTag? {
        return databaseDataSource.getTagByName(name)
    }

    override suspend fun createTag(value: String, emoji: String?): CheckieTag {
        val tag = CheckieTag(id = randomUuid(), value = value, emoji = emoji)
        databaseDataSource.createTag(tag)

        return tag
    }

    override suspend fun updateTag(id: String, value: String, emoji: String?): CheckieTag {
        val tag = CheckieTag(id = id, value = value, emoji = emoji)
        databaseDataSource.updateTag(tag)

        return tag
    }

    override suspend fun deleteTag(id: String) {
        databaseDataSource.deleteTag(id)
    }

    override suspend fun getLatestTagSortingStrategy(): TagSortingStrategy? {
        return preferencesDataSource.getLatestTagSortingStrategy()
    }

    override suspend fun setLatestTagSortingStrategy(strategy: TagSortingStrategy) {
        preferencesDataSource.setLatestTagSortingStrategy(strategy)
    }
}