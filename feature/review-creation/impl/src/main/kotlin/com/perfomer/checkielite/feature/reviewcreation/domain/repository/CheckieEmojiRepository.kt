package com.perfomer.checkielite.feature.reviewcreation.domain.repository

import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmoji
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmojiCategory

internal interface CheckieEmojiRepository {

    suspend fun getCategorizedEmojis(): List<Pair<CheckieEmojiCategory, List<CheckieEmoji>>>
}