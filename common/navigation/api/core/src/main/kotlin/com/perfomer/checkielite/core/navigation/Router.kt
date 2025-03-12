package com.perfomer.checkielite.core.navigation

/**
 * Router that performs navigation throughout the app screens
 */
interface Router {

    /**
     * Navigates to the new screen
     *
     * @param destination screen
     * @param mode type of navigation
     */
    fun navigate(
        destination: Destination,
        mode: DestinationMode = DestinationMode.USUAL,
    )

    /**
     * Replaces current screen with the new one
     *
     * @param destination screen
     * @param mode type of navigation
     */
    fun replace(
        destination: Destination,
        mode: DestinationMode = DestinationMode.USUAL,
    )

    /**
     * Replaces all screens on stack with new screen
     *
     * @param destination screen
     */
    fun replaceStack(destination: Destination)

    /**
     * Navigates to destination and returns result
     *
     * This method guarantees that you will receive the result or `null` on the screen exit.
     * Warning: Result type cast is not safe (unchecked), so use it carefully.
     *
     * @param T type of the result (unsafe cast)
     * @param destination screen
     * @param mode type of navigation
     *
     * @return result or null if screen was closed without result
     */
    suspend fun <T : Result> navigateForResult(
        destination: Destination,
        mode: DestinationMode = DestinationMode.USUAL,
    ): T?

    /**
     * Closes current screen
     */
    fun exit()

    /**
     * Closes current screen with result
     *
     * @param result result
     */
    fun exitWithResult(result: Result)
}