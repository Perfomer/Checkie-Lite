package com.perfomer.checkielite.core.data.repository

import android.content.Context
import android.content.res.AssetManager
import com.perfomer.checkielite.core.data.entity.EmojiCategoryJson
import com.perfomer.checkielite.core.data.mapper.toDomain
import com.perfomer.checkielite.core.domain.entity.emoji.EmojiCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal class EmojiRepositoryImpl(
    private val context: Context,
) : EmojiRepository {

    private var emojis: List<EmojiCategory>? = null
    private val mutex: Mutex = Mutex()

    override suspend fun warmUp() = mutex.withLock {
        if (emojis != null) return@withLock

        withContext(Dispatchers.Default) {
            val emojisJson = context.assets.readAssetsFile("emoji.json")
            emojis = Json.decodeFromString<List<EmojiCategoryJson>>(emojisJson)
                .map { it.toDomain() }
        }
    }

    override suspend fun getCategorizedEmojis(): List<EmojiCategory> {
        if (emojis == null) warmUp()
        return emojis!!
    }

    private companion object {

        private fun AssetManager.readAssetsFile(fileName: String): String = open(fileName).bufferedReader().use { it.readText() }
    }
}
