package com.perfomer.checkielite.update

internal interface AppUpdateManager {

    suspend fun checkUpdateAvailability(): Boolean

    fun launchUpdate()
}

internal suspend fun AppUpdateManager.updateIfAvailable() {
    if (!checkUpdateAvailability()) return
    launchUpdate()
}