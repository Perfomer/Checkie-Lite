package com.perfomer.checkielite.core.navigation.api

interface Router {

	fun navigate(
        screen: CheckieScreen,
		mode: DestinationMode = DestinationMode.USUAL,
	)

	fun replace(
        screen: CheckieScreen,
		mode: DestinationMode = DestinationMode.USUAL,
	)

	fun replaceStack(screen: CheckieScreen)

	suspend fun <T> navigateForResult(
        screen: CheckieScreen,
		mode: DestinationMode = DestinationMode.USUAL,
	): T

	fun exit()

	fun exitWithResult(result: Any?)
}