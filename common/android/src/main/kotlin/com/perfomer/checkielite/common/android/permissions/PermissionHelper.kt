package com.perfomer.checkielite.common.android.permissions

import androidx.activity.result.contract.ActivityResultContracts
import com.perfomer.checkielite.common.android.SingleActivityHolder
import com.perfomer.checkielite.common.android.SuspendableActivityResultHandler
import com.perfomer.checkielite.common.android.util.checkSelfPermissionGranted

interface PermissionHelper {

    fun register()

    suspend fun requestPermission(permission: String): Boolean
}

internal class PermissionHelperImpl(
    private val singleActivityHolder: SingleActivityHolder
) : PermissionHelper {

    private lateinit var permissionResultHandler: SuspendableActivityResultHandler<String, Boolean>

    override fun register() {
        permissionResultHandler = SuspendableActivityResultHandler(
            singleActivityHolder = singleActivityHolder,
            contract = ActivityResultContracts.RequestPermission(),
        )
    }

    override suspend fun requestPermission(permission: String): Boolean {
        if (singleActivityHolder.activity.checkSelfPermissionGranted(permission)) {
            return true
        }

        return permissionResultHandler.awaitResultFor(permission)
    }

    // todo use
    sealed interface PermissionRequestResult {
        data object Granted : PermissionRequestResult
        class Denied(val permanent: Boolean) : PermissionRequestResult
        data object Canceled : PermissionRequestResult
    }
}