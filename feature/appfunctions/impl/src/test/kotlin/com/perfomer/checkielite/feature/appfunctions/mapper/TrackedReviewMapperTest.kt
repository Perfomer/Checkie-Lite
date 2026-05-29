package com.perfomer.checkielite.feature.appfunctions.mapper

import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.feature.appfunctions.entity.TrackedReview
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Date

class TrackedReviewMapperTest {

    @Test
    fun `maps checkie review to tracked review`() {
        val source = CheckieReview(
            id = "42",
            productName = "Cola",
            productBrand = "Darkside",
            price = null,
            rating = 10,
            pictures = emptyList(),
            tags = emptyList(),
            comment = "great",
            advantages = "fizzy",
            disadvantages = "sweet",
            creationDate = Date(0),
            modificationDate = Date(0),
            isSyncing = false,
        )

        val result = source.toTrackedReview()

        assertEquals(
            TrackedReview(
                id = "42",
                productName = "Cola",
                productBrand = "Darkside",
                rating = 10,
                comment = "great",
            ),
            result,
        )
    }

    @Test
    fun `preserves null brand and null comment`() {
        val source = CheckieReview(
            id = "99",
            productName = "Soda",
            productBrand = null,
            price = null,
            rating = 5,
            pictures = emptyList(),
            tags = emptyList(),
            comment = null,
            advantages = null,
            disadvantages = null,
            creationDate = Date(0),
            modificationDate = Date(0),
            isSyncing = false,
        )

        val result = source.toTrackedReview()

        assertEquals(
            TrackedReview(
                id = "99",
                productName = "Soda",
                productBrand = null,
                rating = 5,
                comment = null,
            ),
            result,
        )
    }
}
