package com.perfomer.checkielite.feature.reviewcreation.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen

sealed interface ReviewCreationResult {

    data object Success : ReviewCreationResult
}

fun interface ReviewCreationScreenProvider {

    operator fun invoke(): CheckieScreen
}