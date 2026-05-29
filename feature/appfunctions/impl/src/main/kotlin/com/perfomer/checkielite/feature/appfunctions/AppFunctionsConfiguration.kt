package com.perfomer.checkielite.feature.appfunctions

import androidx.appfunctions.service.AppFunctionConfiguration
import com.perfomer.checkielite.core.data.repository.ReviewRepository

/**
 * Builds the [AppFunctionConfiguration] that wires [CheckieAppFunctions] dependencies.
 * The library instantiates the enclosing class lazily via this factory on each call.
 */
fun checkieAppFunctionConfiguration(
    reviewRepository: ReviewRepository,
): AppFunctionConfiguration {
    return AppFunctionConfiguration.Builder()
        .addEnclosingClassFactory(CheckieAppFunctions::class.java) {
            CheckieAppFunctions(reviewRepository)
        }
        .build()
}
