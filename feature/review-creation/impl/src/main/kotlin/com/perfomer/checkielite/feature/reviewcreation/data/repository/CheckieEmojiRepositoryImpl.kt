package com.perfomer.checkielite.feature.reviewcreation.data.repository

import android.content.Context
import android.content.res.AssetManager
import com.perfomer.checkielite.feature.reviewcreation.data.entity.EmojiCategoryApi
import com.perfomer.checkielite.feature.reviewcreation.data.mapper.toDomain
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmojiCategory
import com.perfomer.checkielite.feature.reviewcreation.domain.repository.CheckieEmojiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal class CheckieEmojiRepositoryImpl(
    private val context: Context,
) : CheckieEmojiRepository {

    private var emojis: List<CheckieEmojiCategory>? = null
    private val mutex: Mutex = Mutex()

    override suspend fun warmUp() = mutex.withLock {
        if (emojis != null) return@withLock

        withContext(Dispatchers.Default) {
            val emojisJson = context.assets.readAssetsFile("emoji.json")
            emojis = Json.decodeFromString<List<EmojiCategoryApi>>(emojisJson)
                .map { it.toDomain() }
        }
    }

    override suspend fun getCategorizedEmojis(): List<CheckieEmojiCategory> {
        if (emojis == null) warmUp()
        return emojis!!
    }
}

private fun AssetManager.readAssetsFile(fileName: String): String = open(fileName).bufferedReader().use { it.readText() }
