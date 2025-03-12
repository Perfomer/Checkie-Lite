package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.core.entity.sort.TagSortingStrategy
import kotlinx.coroutines.flow.Flow

interface TagRepository {

    fun getTags(
        searchQuery: String = "",
        maxCount: Int = Int.MAX_VALUE
    ): Flow<List<CheckieTag>>

    suspend fun getTag(id: String): CheckieTag

    suspend fun getTagByName(name: String): CheckieTag?

    suspend fun createTag(value: String, emoji: String?): CheckieTag

    suspend fun updateTag(id: String, value: String, emoji: String?): CheckieTag

    suspend fun deleteTag(id: String)

    suspend fun getLatestTagSortingStrategy(): TagSortingStrategy?

    suspend fun setLatestTagSortingStrategy(strategy: TagSortingStrategy)
}