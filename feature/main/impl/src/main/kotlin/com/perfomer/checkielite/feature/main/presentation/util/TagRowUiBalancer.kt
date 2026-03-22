package com.perfomer.checkielite.feature.main.presentation.util

import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.Tag
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

internal object TagRowUiBalancer {

    private const val EMOJI_WEIGHT = 3

    fun split(
        tags: ImmutableList<Tag>,
        secondRowThreshold: Int = 10,
    ): Pair<ImmutableList<Tag>, ImmutableList<Tag>?> {
        if (tags.size < secondRowThreshold) {
            return tags to null
        }

        val firstRow = mutableListOf<Tag>()
        val secondRow = mutableListOf<Tag>()

        var firstRowWeight = 0
        var secondRowWeight = 0

        for (tag in tags) {
            val tagWeight = tag.value.length + if (tag.emoji != null) EMOJI_WEIGHT else 0

            if (firstRowWeight <= secondRowWeight) {
                firstRow += tag
                firstRowWeight += tagWeight
            } else {
                secondRow += tag
                secondRowWeight += tagWeight
            }
        }

        return firstRow.toPersistentList() to secondRow.toPersistentList()
    }
}
