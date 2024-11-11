package com.perfomer.checkielite.core.navigation

interface Router {

    fun navigate(
        destination: Destination,
        mode: DestinationMode = DestinationMode.USUAL,
    )

    fun replace(
        destination: Destination,
        mode: DestinationMode = DestinationMode.USUAL,
    )

    fun replaceStack(destination: Destination)

    suspend fun <T> navigateForResult(
        destination: Destination,
        mode: DestinationMode = DestinationMode.USUAL,
    ): T

    fun exit()

    fun exitWithResult(result: Any?)
}