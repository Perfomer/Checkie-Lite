package com.perfomer.checkielite.feature.reviewdetails.data.repository

import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.reviewdetails.domain.repository.ReviewRepository
import java.util.Date

internal class ReviewRepositoryImpl : ReviewRepository {

    override suspend fun getReview(reviewId: String): CheckieReview {
        return CheckieReview(
            id = reviewId,
            productName = "Chicken toasts with poached eggs",
            productBrand = "Lui Bidon",
            rating = 8,
            picturesUri = listOf(
                "https://habrastorage.org/r/w780/getpro/habr/upload_files/746/2ab/27c/7462ab27cca552ce31ee9cba01387692.jpeg",
                "https://images.unsplash.com/photo-1483129804960-cb1964499894?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
                "https://images.unsplash.com/photo-1620447875063-19be4e4604bc?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=796&q=80",
                "https://images.unsplash.com/photo-1548100535-fe8a16c187ef?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1151&q=80",
            ),
            reviewText = "Extraordinary. Meets an elite standard by which you judge all other restaurants. The staff is always ready to help, the premises are extremely clean, the atmosphere is lovely, and the food is both delicious and beautifully presented.",
            creationDate = Date(),
        )
    }
}