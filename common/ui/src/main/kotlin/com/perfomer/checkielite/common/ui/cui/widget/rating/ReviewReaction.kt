package com.perfomer.checkielite.common.ui.cui.widget.rating

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.perfomer.checkielite.common.ui.R

enum class ReviewReaction(
    @DrawableRes val drawable: Int,
    @StringRes val contentDescription: Int,
) {
    DISGUSTING(
        drawable = R.drawable.emoji_disgusting,
        contentDescription = R.string.common_reaction_disgusting,
    ),
    DISAPPOINTED(
        drawable = R.drawable.emoji_disappointed,
        contentDescription = R.string.common_reaction_disappointed,
    ),
    OKAY(
        drawable = R.drawable.emoji_okay,
        contentDescription = R.string.common_reaction_okay,
    ),
    LIKE_IT(
        drawable = R.drawable.emoji_like_it,
        contentDescription = R.string.common_reaction_like_it,
    ),
    BRILLIANT(
        drawable = R.drawable.emoji_brilliant,
        contentDescription = R.string.common_reaction_brilliant,
    );

    companion object {

        fun createFromRating(rating: Int): ReviewReaction {
            return when (rating) {
                0 -> DISGUSTING
                1, 2, 3 -> DISAPPOINTED
                4, 5, 6 -> OKAY
                7, 8, 9 -> LIKE_IT
                10 -> BRILLIANT
                else -> OKAY
            }
        }
    }
}