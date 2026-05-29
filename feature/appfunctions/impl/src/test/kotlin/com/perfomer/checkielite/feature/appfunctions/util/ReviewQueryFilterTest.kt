package com.perfomer.checkielite.feature.appfunctions.util

import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Date

class ReviewQueryFilterTest {

    private fun review(id: String, name: String, brand: String?, creationDate: Date = Date(0)): CheckieReview =
        CheckieReview(
            id = id,
            productName = name,
            productBrand = brand,
            price = null,
            rating = 5,
            pictures = emptyList(),
            tags = emptyList(),
            comment = null,
            advantages = null,
            disadvantages = null,
            creationDate = creationDate,
            modificationDate = Date(0),
            isSyncing = false,
        )

    private val reviews = listOf(
        review("1", "Cola", "Darkside"),
        review("2", "Lemonade", "Musthave"),
        review("3", "Cola Zero", null),
    )

    @Test
    fun `null or blank query returns all`() {
        assertEquals(3, filterReviews(reviews, null).size)
        assertEquals(3, filterReviews(reviews, "   ").size)
    }

    @Test
    fun `matches brand case-insensitively`() {
        val result = filterReviews(reviews, "darkside")
        assertEquals(listOf("1"), result.map { it.id })
    }

    @Test
    fun `matches product name substring`() {
        val result = filterReviews(reviews, "cola")
        assertEquals(listOf("1", "3"), result.map { it.id })
    }

    @Test
    fun `caps results at the limit and returns the newest first`() {
        val dated = listOf(
            review("old", "Cola 1", null, creationDate = Date(1)),
            review("mid", "Cola 2", null, creationDate = Date(2)),
            review("new", "Cola 3", null, creationDate = Date(3)),
        )

        val result = filterReviews(dated, query = null, limit = 2)

        assertEquals(listOf("new", "mid"), result.map { it.id })
    }
}
