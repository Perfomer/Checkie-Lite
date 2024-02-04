package com.perfomer.checkielite.core.navigation.api

enum class ExternalDestination {
	CAMERA,
	GALLERY
}

sealed class ExternalResult {

	data class Success(val path: String) : ExternalResult()

	data object Cancel : ExternalResult()
}

interface ExternalRouter {

	suspend fun navigateForResult(destination: ExternalDestination): ExternalResult
}