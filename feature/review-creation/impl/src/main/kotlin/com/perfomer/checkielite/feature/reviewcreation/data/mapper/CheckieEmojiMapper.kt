package com.perfomer.checkielite.feature.reviewcreation.data.mapper

import com.perfomer.checkielite.feature.reviewcreation.data.entity.EmojiApi
import com.perfomer.checkielite.feature.reviewcreation.data.entity.EmojiCategoryApi
import com.perfomer.checkielite.feature.reviewcreation.data.entity.EmojiGroupApi
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmoji
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmojiCategory
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmojiGroup

internal fun EmojiCategoryApi.toDomain(): CheckieEmojiCategory {
    return CheckieEmojiCategory(
        name = name,
        groups = groups.map { it.toDomain() },
    )
}

private fun EmojiGroupApi.toDomain(): CheckieEmojiGroup {
    return CheckieEmojiGroup(
        name = name,
        emojis = emojis.map { it.toDomain() },
    )
}

private fun EmojiApi.toDomain(): CheckieEmoji {
    return CheckieEmoji(
        id = codes,
        char = char,
        name = name,
    )
}