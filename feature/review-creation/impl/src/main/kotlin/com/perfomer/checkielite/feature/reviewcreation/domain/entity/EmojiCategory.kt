package com.perfomer.checkielite.feature.reviewcreation.domain.entity

internal data class EmojiCategory(
    val type: EmojiCategoryType,
    val groups: List<EmojiGroup>,
)

internal enum class EmojiCategoryType(
    val originalName: String,
) {
    SMILEYS_AND_EMOTION("Smileys & Emotion"),
    PEOPLE_AND_BODY("People & Body"),
    ANIMALS_AND_NATURE("Animals & Nature"),
    FOOD_AND_DRINK("Food & Drink"),
    TRAVEL_AND_PLACES("Travel & Places"),
    ACTIVITIES("Activities"),
    OBJECTS("Objects"),
    SYMBOLS("Symbols"),
    FLAGS("Flags"),
}