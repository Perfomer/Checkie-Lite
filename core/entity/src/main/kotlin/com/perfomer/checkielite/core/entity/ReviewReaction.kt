package com.perfomer.checkielite.core.entity

enum class ReviewReaction(val emoji: String) {
    DISGUSTING("\uD83D\uDCA9"),
    DISAPPOINTED("\uD83D\uDE2D"),
    OKAY("\uD83D\uDE10"),
    LIKE_IT("\uD83D\uDE0D"),
    BRILLIANT("\uD83D\uDC8E");

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