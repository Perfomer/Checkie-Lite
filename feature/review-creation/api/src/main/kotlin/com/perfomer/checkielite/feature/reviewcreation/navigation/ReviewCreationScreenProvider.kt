package com.perfomer.checkielite.feature.reviewcreation.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationMode

class ReviewCreationParams(val mode: ReviewCreationMode) : Params

sealed interface ReviewCreationResult {

    data object Success : ReviewCreationResult
}

fun interface ReviewCreationScreenProvider {

    operator fun invoke(params: ReviewCreationParams): CheckieScreen
}