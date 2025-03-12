package com.perfomer.checkielite.feature.reviewcreation.presentation.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.perfomer.checkielite.core.domain.entity.emoji.EmojiCategoryType
import com.perfomer.checkielite.feature.reviewcreation.R

@get:DrawableRes
internal val EmojiCategoryType.iconResource: Int
    get() = when (this) {
        EmojiCategoryType.SMILEYS_AND_EMOTION -> R.drawable.ic_emoji_category_smiles
        EmojiCategoryType.PEOPLE_AND_BODY -> R.drawable.ic_emoji_category_body
        EmojiCategoryType.ANIMALS_AND_NATURE -> R.drawable.ic_emoji_category_animals
        EmojiCategoryType.FOOD_AND_DRINK -> R.drawable.ic_emoji_category_food
        EmojiCategoryType.TRAVEL_AND_PLACES -> R.drawable.ic_emoji_category_travel
        EmojiCategoryType.ACTIVITIES -> R.drawable.ic_emoji_category_activities
        EmojiCategoryType.OBJECTS -> R.drawable.ic_emoji_category_objects
        EmojiCategoryType.SYMBOLS -> R.drawable.ic_emoji_category_symbols
        EmojiCategoryType.FLAGS -> R.drawable.ic_emoji_category_flags
    }

@get:StringRes
internal val EmojiCategoryType.nameResource: Int
    get() = when (this) {
        EmojiCategoryType.SMILEYS_AND_EMOTION -> R.string.tagcreation_emoji_category_smiles
        EmojiCategoryType.PEOPLE_AND_BODY -> R.string.tagcreation_emoji_category_body
        EmojiCategoryType.ANIMALS_AND_NATURE -> R.string.tagcreation_emoji_category_animals
        EmojiCategoryType.FOOD_AND_DRINK -> R.string.tagcreation_emoji_category_food
        EmojiCategoryType.TRAVEL_AND_PLACES -> R.string.tagcreation_emoji_category_travel
        EmojiCategoryType.ACTIVITIES -> R.string.tagcreation_emoji_category_activities
        EmojiCategoryType.OBJECTS -> R.string.tagcreation_emoji_category_objects
        EmojiCategoryType.SYMBOLS -> R.string.tagcreation_emoji_category_symbols
        EmojiCategoryType.FLAGS -> R.string.tagcreation_emoji_category_flags
    }