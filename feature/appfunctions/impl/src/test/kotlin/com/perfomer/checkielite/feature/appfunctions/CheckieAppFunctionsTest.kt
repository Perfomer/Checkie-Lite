package com.perfomer.checkielite.feature.appfunctions

import android.content.Context
import androidx.appfunctions.AppFunctionContext
import com.perfomer.checkielite.core.data.repository.ReviewRepository
import com.perfomer.checkielite.core.domain.entity.price.CheckiePrice
import com.perfomer.checkielite.core.domain.entity.review.CheckiePicture
import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Date

/**
 * Local JVM test of the @AppFunction methods themselves (not just the extracted pure helpers).
 *
 * alpha09 does not ship `AppFunctionTestRule` (it existed in alpha03 and was removed), so we
 * exercise [CheckieAppFunctions] directly: a fake in-memory [ReviewRepository] and a fake
 * [AppFunctionContext] (the methods never touch the context). No device or Robolectric needed.
 */
class CheckieAppFunctionsTest {

    private val fakeContext = object : AppFunctionContext {
        override val context: Context get() = error("Context is not used by CheckieAppFunctions")
    }

    private val repository = FakeReviewRepository()
    private val functions = CheckieAppFunctions(repository)

    @Test
    fun `trackReview creates a review and returns it with a generated id`() = runBlocking {
        val result = functions.trackReview(
            appFunctionContext = fakeContext,
            productName = "Cola",
            productBrand = "Darkside",
            rating = 10,
            comment = "great",
        )

        assertTrue(result.id.isNotBlank())
        assertEquals("Cola", result.productName)
        assertEquals("Darkside", result.productBrand)
        assertEquals(10, result.rating)
        assertEquals("great", result.comment)
        assertEquals(1, repository.getReviews().value.size)
    }

    @Test
    fun `trackReview coerces an out-of-range rating into 0 to 10`() = runBlocking {
        val result = functions.trackReview(
            appFunctionContext = fakeContext,
            productName = "Lemonade",
            productBrand = "Musthave",
            rating = 15,
        )

        assertEquals(10, result.rating)
        assertEquals(10, repository.getReviews().value.single().rating)
    }

    @Test
    fun `trackReview rejects a blank product name`() {
        assertThrows<IllegalArgumentException> {
            runBlocking {
                functions.trackReview(
                    appFunctionContext = fakeContext,
                    productName = "  ",
                    rating = 5,
                )
            }
        }
    }

    @Test
    fun `trackReview returns the newest record when an identical review already exists`() = runBlocking {
        functions.trackReview(
            fakeContext,
            productName = "Cola",
            productBrand = "Darkside",
            rating = 10,
        )
        val second = functions.trackReview(
            fakeContext,
            productName = "Cola",
            productBrand = "Darkside",
            rating = 10,
        )

        assertEquals(2, repository.getReviews().value.size)
        assertEquals("id-2", second.id)
    }

    @Test
    fun `trackReview throws when the created review cannot be retrieved`() {
        val brokenFunctions = CheckieAppFunctions(FakeReviewRepository(persistOnCreate = false))

        assertThrows<IllegalStateException> {
            runBlocking {
                brokenFunctions.trackReview(
                    appFunctionContext = fakeContext,
                    productName = "Cola",
                    rating = 5,
                )
            }
        }
    }

    @Test
    fun `findReviews filters by query across brand and name`() = runBlocking {
        functions.trackReview(
            fakeContext,
            productName = "Cola",
            productBrand = "Darkside",
            rating = 8
        )
        functions.trackReview(
            fakeContext,
            productName = "Lemonade",
            productBrand = "Musthave",
            rating = 6
        )

        val result = functions.findReviews(fakeContext, query = "darkside")

        assertEquals(listOf("Cola"), result.map { it.productName })
    }

    @Test
    fun `findReviews without a query returns all reviews`() = runBlocking {
        functions.trackReview(
            fakeContext,
            productName = "Cola",
            productBrand = "Darkside",
            rating = 8
        )
        functions.trackReview(
            fakeContext,
            productName = "Lemonade",
            productBrand = "Musthave",
            rating = 6
        )

        val result = functions.findReviews(fakeContext, query = null)

        assertEquals(2, result.size)
    }

    /**
     * Minimal in-memory [ReviewRepository]; only the methods used by [CheckieAppFunctions] do real work.
     * [persistOnCreate] = false models a repository where the created review cannot be read back.
     */
    private class FakeReviewRepository(
        private val persistOnCreate: Boolean = true,
    ) : ReviewRepository {

        private val reviews = MutableStateFlow<List<CheckieReview>>(emptyList())
        private var sequence = 0L

        override fun getReviews(): MutableStateFlow<List<CheckieReview>> = reviews

        override fun getReviewsByBrand(brand: String): Flow<List<CheckieReview>> =
            reviews.map { list -> list.filter { it.productBrand == brand } }

        override fun getReview(reviewId: String): Flow<CheckieReview> =
            reviews.map { list -> list.first { it.id == reviewId } }

        override suspend fun createReview(
            productName: String,
            productBrand: String?,
            price: CheckiePrice?,
            rating: Int,
            pictures: List<CheckiePicture>,
            tagsIds: Set<String>,
            comment: String?,
            advantages: String?,
            disadvantages: String?,
        ) {
            if (!persistOnCreate) return

            sequence++
            reviews.value += CheckieReview(
                id = "id-$sequence",
                productName = productName,
                productBrand = productBrand,
                price = price,
                rating = rating,
                pictures = pictures,
                tags = emptyList(),
                comment = comment,
                advantages = advantages,
                disadvantages = disadvantages,
                creationDate = Date(sequence),
                modificationDate = Date(sequence),
                isSyncing = false,
            )
        }

        override suspend fun updateReview(
            reviewId: String,
            productName: String,
            productBrand: String?,
            price: CheckiePrice?,
            rating: Int,
            pictures: List<CheckiePicture>,
            tagsIds: Set<String>,
            comment: String?,
            advantages: String?,
            disadvantages: String?,
        ) = Unit

        override suspend fun deleteReview(reviewId: String) = Unit
    }
}
