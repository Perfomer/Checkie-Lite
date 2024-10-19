package com.perfomer.checkielite.update

import android.util.Log
import com.perfomer.checkielite.common.pure.util.safeResume
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.rustore.sdk.appupdate.listener.InstallStateUpdateListener
import ru.rustore.sdk.appupdate.manager.RuStoreAppUpdateManager
import ru.rustore.sdk.appupdate.model.AppUpdateInfo
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.InstallState
import ru.rustore.sdk.appupdate.model.InstallStatus
import ru.rustore.sdk.appupdate.model.UpdateAvailability

internal class RuStoreUpdateManager(
    private val updateManager: RuStoreAppUpdateManager,
) : AppUpdateManager, InstallStateUpdateListener {

    private var appUpdateInfo: AppUpdateInfo? = null
    private val appUpdateOptions = AppUpdateOptions.Builder().build()

    override suspend fun checkUpdateAvailability(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            updateManager
                .getAppUpdateInfo()
                .addOnSuccessListener { info ->
                    appUpdateInfo = info
                    val isUpdateAvailable = info.updateAvailability == UpdateAvailability.UPDATE_AVAILABLE
                    continuation.safeResume(isUpdateAvailable)
                }
                .addOnFailureListener { throwable ->
                    Log.w(TAG, "Failed to check if update is available", throwable)
                    continuation.safeResume(false)
                }
        }
    }

    override fun launchUpdate() {
        updateManager.registerListener(this)
        startUpdate(appUpdateOptions)
    }

    override fun onStateUpdated(state: InstallState) {
        when (state.installStatus) {
            InstallStatus.DOWNLOADED -> {
                updateManager.completeUpdate(appUpdateOptions)
                updateManager.unregisterListener(this)
            }
            InstallStatus.FAILED -> {
                Log.w(TAG, "Failed to install update")
                updateManager.unregisterListener(this)
            }
        }
    }

    private fun startUpdate(options: AppUpdateOptions) {
        updateManager.startUpdateFlow(
            appUpdateInfo = appUpdateInfo ?: return,
            appUpdateOptions = options,
        )
            .addOnFailureListener { throwable ->
                Log.w(TAG, "Failed to launch app update", throwable)
                updateManager.unregisterListener(this)
            }
    }

    private companion object {
        private const val TAG = "RuStoreUpdateManager"
    }
}