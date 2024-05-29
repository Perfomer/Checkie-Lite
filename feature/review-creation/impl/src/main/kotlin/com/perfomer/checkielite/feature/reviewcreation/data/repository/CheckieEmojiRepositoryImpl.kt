package com.perfomer.checkielite.feature.reviewcreation.data.repository

import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmoji
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmojiCategory
import com.perfomer.checkielite.feature.reviewcreation.domain.repository.CheckieEmojiRepository

internal class CheckieEmojiRepositoryImpl : CheckieEmojiRepository {

    override suspend fun getCategorizedEmojis(): List<Pair<CheckieEmojiCategory, List<CheckieEmoji>>> {
        return emptyList() // TODO
    }
}