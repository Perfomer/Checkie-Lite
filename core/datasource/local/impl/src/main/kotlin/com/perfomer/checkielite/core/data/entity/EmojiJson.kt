package com.perfomer.checkielite.core.data.entity

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
internal data class EmojiCategoryJson(
    val name: String,
    val groups: @Contextual List<EmojiGroupJson>
)

@Serializable
internal data class EmojiGroupJson(
    val name: String,
    val emojis: List<EmojiJson>,
)

@Serializable
internal data class EmojiJson(
    val codes: String,
    val char: String,
    val name: String,
)