package com.perfomer.checkielite.feature.reviewcreation.domain.repository

import com.perfomer.checkielite.feature.reviewcreation.domain.entity.EmojiCategory

internal interface CheckieEmojiRepository {

    suspend fun warmUp()

    suspend fun getCategorizedEmojis(): List<EmojiCategory>
}