package com.perfomer.checkielite.feature.reviewcreation.data.repository

import com.chrynan.emoji.core.Emoji
import com.chrynan.emoji.core.allEmojis
import com.chrynan.emoji.core.categorize
import com.chrynan.emoji.repo.map.KotlinMapEmojiRepository
import com.chrynan.emoji.repo.map.init
import com.perfomer.checkielite.feature.reviewcreation.data.mapper.toDomain
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmoji
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmojiCategory
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

    override suspend fun getCategorizedEmojis(): List<Pair<CheckieEmojiCategory, List<CheckieEmoji>>> {
        return withContext(Dispatchers.IO) {
            emojiRepository.getAll().toList().categorize()
                .map { category ->
                    val checkieCategory = category.toDomain()
                    val checkieEmojis = category.allEmojis()
                        .filterIsInstance<Emoji.Unicode>()
                        .map { it.toDomain() }

                    checkieCategory to checkieEmojis
                }
        }
    }
}