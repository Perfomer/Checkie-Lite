package com.perfomer.checkielite.feature.reviewcreation.data.entity

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
internal data class EmojiCategoryApi(
    val name: String,
    val groups: @Contextual List<EmojiGroupApi>
)

@Serializable
internal data class EmojiGroupApi(
    val name: String,
    val emojis: List<EmojiApi>,
)

@Serializable
internal data class EmojiApi(
    val codes: String,
    val char: String,
    val name: String,
)