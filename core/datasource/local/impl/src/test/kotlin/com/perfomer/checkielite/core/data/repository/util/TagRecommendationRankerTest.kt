package com.perfomer.checkielite.core.data.repository.util

import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.core.domain.entity.review.CheckieTag
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Date

internal class TagRecommendationRankerTest {

    private val ranker = TagRecommendationRanker()

    @Test
    fun `GIVEN empty signals WHEN ranking THEN return empty list`() {
        val result = ranker.rank(
            reviews = listOf(review("1", tags = listOf(tagFood))),
            selectedTagIds = emptySet(),
            productBrand = "",
            maxCount = 5,
        )

        assertEquals(emptyList<CheckieTag>(), result)
    }

    @Test
    fun `GIVEN selected tag WHEN ranking THEN sort by cooccurrence`() {
        val result = ranker.rank(
            reviews = listOf(
                review("1", tags = listOf(tagMeat, tagFood)),
                review("2", tags = listOf(tagMeat, tagFood)),
                review("3", tags = listOf(tagMeat, tagSpicy)),
            ),
            selectedTagIds = setOf(tagMeat.id),
            productBrand = "",
            maxCount = 5,
        )

        assertEquals(listOf(tagFood, tagSpicy), result)
    }

    @Test
    fun `GIVEN brand WHEN ranking THEN return brand recommendations`() {
        val result = ranker.rank(
            reviews = listOf(
                review("1", brand = "McDonalds", tags = listOf(tagRestaurant)),
                review("2", brand = "McDonalds", tags = listOf(tagRestaurant, tagFastfood)),
                review("3", brand = "KFC", tags = listOf(tagFastfood)),
            ),
            selectedTagIds = emptySet(),
            productBrand = "McDonalds",
            maxCount = 5,
        )

        assertEquals(listOf(tagRestaurant, tagFastfood), result)
    }

    @Test
    fun `GIVEN several signals WHEN ranking THEN combine scores and exclude selected tags`() {
        val result = ranker.rank(
            reviews = listOf(
                review("1", brand = "McDonalds", tags = listOf(tagMeat, tagFood, tagRestaurant)),
                review("2", brand = "McDonalds", tags = listOf(tagMeat, tagRestaurant)),
                review("3", brand = "McDonalds", tags = listOf(tagFood, tagRestaurant)),
                review("4", brand = "Other", tags = listOf(tagMeat, tagSpicy)),
            ),
            selectedTagIds = setOf(tagMeat.id),
            productBrand = "McDonalds",
            maxCount = 5,
        )

        assertEquals(listOf(tagRestaurant, tagFood, tagSpicy), result)
    }

    @Test
    fun `GIVEN max count WHEN ranking THEN trim result`() {
        val result = ranker.rank(
            reviews = listOf(
                review("1", tags = listOf(tagMeat, tagFood)),
                review("2", tags = listOf(tagMeat, tagRestaurant)),
                review("3", tags = listOf(tagMeat, tagSpicy)),
            ),
            selectedTagIds = setOf(tagMeat.id),
            productBrand = "",
            maxCount = 2,
        )

        assertEquals(listOf(tagFood, tagRestaurant), result)
    }

    private fun review(
        id: String,
        brand: String? = null,
        tags: List<CheckieTag>,
    ) = CheckieReview(
        id = id,
        productName = "Product$id",
        productBrand = brand,
        price = null,
        rating = 5,
        pictures = emptyList(),
        tags = tags,
        comment = null,
        advantages = null,
        disadvantages = null,
        creationDate = Date(0),
        modificationDate = Date(0),
        isSyncing = false,
    )

    private companion object {
        val tagMeat = CheckieTag(id = "meat", value = "Meat", emoji = null)
        val tagFood = CheckieTag(id = "food", value = "Food", emoji = null)
        val tagRestaurant = CheckieTag(id = "restaurant", value = "Restaurant", emoji = null)
        val tagFastfood = CheckieTag(id = "fastfood", value = "Fast food", emoji = null)
        val tagSpicy = CheckieTag(id = "spicy", value = "Spicy", emoji = null)
    }
}
