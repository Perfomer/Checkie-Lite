package com.perfomer.checkielite.feature.reviewcreation.data.repository

import com.chrynan.emoji.core.EmojiCategory
import com.chrynan.emoji.core.categorize
import com.chrynan.emoji.repo.map.KotlinMapEmojiRepository
import com.chrynan.emoji.repo.map.init
import com.perfomer.checkielite.feature.reviewcreation.domain.repository.CheckieEmojiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class CheckieEmojiRepositoryImpl(
    private val emojiRepository: KotlinMapEmojiRepository,
) : CheckieEmojiRepository {

    override suspend fun warmUp() {
        withContext(Dispatchers.IO) {
            emojiRepository.init()
        }
    }

    override suspend fun getCategorizedEmojis(): List<EmojiCategory> {
        return withContext(Dispatchers.IO) {
            emojiRepository.getAll().toList().categorize()
        }
    }
}