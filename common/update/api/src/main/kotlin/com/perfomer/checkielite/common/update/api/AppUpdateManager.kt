package com.perfomer.checkielite.common.update.api

interface AppUpdateManager {

    suspend fun checkUpdateAvailability(): Boolean

    fun launchUpdate()
}

suspend fun AppUpdateManager.updateIfAvailable() {
    if (!checkUpdateAvailability()) return
    launchUpdate()
}