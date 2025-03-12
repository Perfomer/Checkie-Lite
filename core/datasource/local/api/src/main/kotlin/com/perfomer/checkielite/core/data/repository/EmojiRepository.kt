package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.core.entity.emoji.EmojiCategory

interface EmojiRepository {

    suspend fun warmUp()

    suspend fun getCategorizedEmojis(): List<EmojiCategory>
}