package com.perfomer.checkielite.feature.reviewcreation.domain.repository

import com.chrynan.emoji.core.EmojiCategory

interface CheckieEmojiRepository {

    suspend fun warmUp()

    suspend fun getCategorizedEmojis(): List<EmojiCategory>
}