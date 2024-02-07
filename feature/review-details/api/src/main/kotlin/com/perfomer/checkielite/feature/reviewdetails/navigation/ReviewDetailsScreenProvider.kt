package com.perfomer.checkielite.feature.reviewdetails.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params

class ReviewDetailsParams(val reviewId: String) : Params

fun interface ReviewDetailsScreenProvider {

    operator fun invoke(params: ReviewDetailsParams): CheckieScreen
}