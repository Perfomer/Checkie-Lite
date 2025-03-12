package com.perfomer.checkielite.core.data.mapper

import com.perfomer.checkielite.core.data.entity.EmojiCategoryJson
import com.perfomer.checkielite.core.data.entity.EmojiGroupJson
import com.perfomer.checkielite.core.data.entity.EmojiJson
import com.perfomer.checkielite.core.domain.entity.emoji.Emoji
import com.perfomer.checkielite.core.domain.entity.emoji.EmojiCategory
import com.perfomer.checkielite.core.domain.entity.emoji.EmojiCategoryType
import com.perfomer.checkielite.core.domain.entity.emoji.EmojiGroup

internal fun EmojiCategoryJson.toDomain(): EmojiCategory {
    return EmojiCategory(
        type = EmojiCategoryType.entries.first { it.originalName == name },
        groups = groups.map { it.toDomain() },
    )
}

private fun EmojiGroupJson.toDomain(): EmojiGroup {
    return EmojiGroup(
        name = name,
        emojis = emojis.map { it.toDomain() },
    )
}

private fun EmojiJson.toDomain(): Emoji {
    return Emoji(
        id = codes,
        char = char,
        name = name,
    )
}