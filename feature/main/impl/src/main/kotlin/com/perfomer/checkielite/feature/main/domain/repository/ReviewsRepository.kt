package com.perfomer.checkielite.feature.main.domain.repository

import com.perfomer.checkielite.core.entity.CheckieReview

internal interface ReviewsRepository {

    suspend fun getCheckies(searchQuery: String = ""): List<CheckieReview>
}