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
     * @param initialContent ephemeral data available while the screen is created
     */
    fun <D : Destination> navigate(
        destination: D,
        mode: DestinationMode = DestinationMode.USUAL,
        initialContent: InitialContent<D>? = null,
    )

    /**
     * Replaces current screen with the new one
     *
     * @param destination screen
     * @param mode type of navigation
     * @param initialContent ephemeral data available while the screen is created
     */
    fun <D : Destination> replace(
        destination: D,
        mode: DestinationMode = DestinationMode.USUAL,
        initialContent: InitialContent<D>? = null,
    )

    /**
     * Replaces all screens on stack with new screen
     *
     * @param destination screen
     * @param initialContent ephemeral data available while the screen is created
     */
    fun <D : Destination> replaceStack(
        destination: D,
        initialContent: InitialContent<D>? = null,
    )

    /**
     * Navigates to destination and returns result
     *
     * This method guarantees that you will receive the result or `null` on the screen exit.
     * Warning: Result type cast is not safe (unchecked), so use it carefully.
     *
     * @param T type of the result (unsafe cast)
     * @param destination screen
     * @param mode type of navigation
     * @param initialContent ephemeral data available while the screen is created
     *
     * @return result or null if screen was closed without result
     */
    suspend fun <T : Result, D : DestinationWithResult<T>> navigateForResult(
        destination: D,
        mode: DestinationMode = DestinationMode.USUAL,
        initialContent: InitialContent<D>? = null,
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
