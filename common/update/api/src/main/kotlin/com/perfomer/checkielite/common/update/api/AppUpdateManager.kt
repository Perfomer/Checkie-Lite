package com.perfomer.checkielite.common.update.api

import com.perfomer.checkielite.common.pure.util.runSuspendCatching

interface AppUpdateManager {

    suspend fun checkUpdateAvailability(): Boolean

    fun launchUpdate()
}

suspend fun AppUpdateManager.updateIfAvailable() = runSuspendCatching {
    if (!checkUpdateAvailability()) return@runSuspendCatching
    launchUpdate()
}