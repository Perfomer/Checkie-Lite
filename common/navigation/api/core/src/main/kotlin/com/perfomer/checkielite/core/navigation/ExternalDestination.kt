package com.perfomer.checkielite.core.navigation

enum class ExternalDestinationWithResult {
    CAMERA,
    GALLERY,
    FILE,
}

enum class ExternalDestination {
    LANGUAGE_SETTINGS,
}

sealed class ExternalResult<T> {

	data class Success<T>(val result: T) : ExternalResult<T>()

	data object Cancel : ExternalResult<Nothing>()
}

interface ExternalRouter {

	suspend fun <T> navigateForResult(destination: ExternalDestinationWithResult): ExternalResult<T>

    fun navigate(destination: ExternalDestination)
}