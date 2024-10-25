package com.perfomer.checkielite.feature.reviewcreation.data.mapper

import com.perfomer.checkielite.feature.reviewcreation.data.entity.EmojiApi
import com.perfomer.checkielite.feature.reviewcreation.data.entity.EmojiCategoryApi
import com.perfomer.checkielite.feature.reviewcreation.data.entity.EmojiGroupApi
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.Emoji
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.EmojiCategory
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.EmojiCategoryType
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.EmojiGroup

internal fun EmojiCategoryApi.toDomain(): EmojiCategory {
    return EmojiCategory(
        type = EmojiCategoryType.entries.first { it.originalName == name },
        groups = groups.map { it.toDomain() },
    )
}

private fun EmojiGroupApi.toDomain(): EmojiGroup {
    return EmojiGroup(
        name = name,
        emojis = emojis.map { it.toDomain() },
    )
}

private fun EmojiApi.toDomain(): Emoji {
    return Emoji(
        id = codes,
        char = char,
        name = name,
    )
}