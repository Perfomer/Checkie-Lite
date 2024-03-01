package com.perfomer.checkielite.core.navigation.api

interface Router {

	fun navigate(
        screen: CheckieScreen,
	)

	fun replace(
        screen: CheckieScreen,
	)

	suspend fun <T> navigateForResult(
        screen: CheckieScreen,
	): T

	fun exit()

	fun exitWithResult(result: Any?)
}