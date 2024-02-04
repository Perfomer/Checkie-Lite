package com.perfomer.checkielite.feature.main.data.repository

import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.main.domain.repository.ReviewsRepository
import kotlinx.datetime.Clock

internal class ReviewsRepositoryImpl : ReviewsRepository {

    override suspend fun getCheckies(searchQuery: String): List<CheckieReview> {
        return listOf(
            CheckieReview(
                id = "1",
                productName = "Chicken toasts with poached eggs",
                productBrand = "Lui Bidon",
                imagesUri = listOf("https://habrastorage.org/r/w780/getpro/habr/upload_files/746/2ab/27c/7462ab27cca552ce31ee9cba01387692.jpeg"),
                rating = 8,
                reviewText = null,
                creationDate = Clock.System.now(),
            ),
            CheckieReview(
                id = "2",
                productName = "Lemonblast",
                productBrand = "Darkside",
                imagesUri = listOf("https://images.unsplash.com/photo-1483129804960-cb1964499894?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"),
                rating = 0,
                reviewText = null,
                creationDate = Clock.System.now(),
            ),
            CheckieReview(
                id = "3",
                productName = "One Flew Over the Cuckoos Next",
                productBrand = "Key Kesey",
                imagesUri = listOf("https://images.unsplash.com/photo-1620447875063-19be4e4604bc?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=796&q=80"),
                rating = 10,
                reviewText = null,
                creationDate = Clock.System.now(),
            ),
            CheckieReview(
                id = "4",
                productName = "The Wolf of Wall Street",
                productBrand = null,
                imagesUri = listOf("https://images.unsplash.com/photo-1548100535-fe8a16c187ef?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1151&q=80"),
                rating = 4,
                reviewText = null,
                creationDate = Clock.System.now(),
            ),
            CheckieReview(
                id = "5",
                productName = "My own dog",
                productBrand = null,
                imagesUri = emptyList(),
                rating = 3,
                reviewText = null,
                creationDate = Clock.System.now(),
            ),
        )
    }
}