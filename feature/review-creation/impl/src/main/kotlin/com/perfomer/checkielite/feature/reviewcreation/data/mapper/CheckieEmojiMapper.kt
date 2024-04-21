package com.perfomer.checkielite.feature.reviewcreation.data.mapper

import com.chrynan.emoji.core.Emoji
import com.chrynan.emoji.core.EmojiCategory
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmoji
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmojiCategory

internal fun Emoji.Unicode.toDomain(): CheckieEmoji {
    return CheckieEmoji(
        emoji = character,
        name = name,
        aliases = aliases,
    )
}

internal fun EmojiCategory.toDomain(): CheckieEmojiCategory {
    val iconEmoji = icon as Emoji.Unicode

    return CheckieEmojiCategory(
        name = requireNotNull(name),
        emoji = iconEmoji.character,
    )
}