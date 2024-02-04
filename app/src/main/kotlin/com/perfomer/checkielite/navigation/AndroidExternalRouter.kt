package com.perfomer.checkielite.navigation

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.perfomer.checkielite.common.android.SingleActivityHolder
import com.perfomer.checkielite.common.android.SuspendableActivityResultHandler
import com.perfomer.checkielite.common.android.util.getRealPath
import com.perfomer.checkielite.core.navigation.api.ExternalDestination
import com.perfomer.checkielite.core.navigation.api.ExternalResult
import com.perfomer.checkielite.core.navigation.api.ExternalRouter

private const val IMAGE_TYPE = "image/*"

internal class AndroidExternalRouter(
    private val singleActivityHolder: SingleActivityHolder
) : ExternalRouter {

    private val activity: AppCompatActivity
        get() = singleActivityHolder.activity

    private lateinit var permissionResultHandler: SuspendableActivityResultHandler<String, Boolean>
    private lateinit var cameraResultHandler: SuspendableActivityResultHandler<Uri, Boolean>
    private lateinit var commonActivityResultHandler: SuspendableActivityResultHandler<Intent, ActivityResult>

    fun register() {
        permissionResultHandler = SuspendableActivityResultHandler(
            singleActivityHolder = singleActivityHolder,
            contract = ActivityResultContracts.RequestPermission(),
        )
        cameraResultHandler = SuspendableActivityResultHandler(
            singleActivityHolder = singleActivityHolder,
            contract = ActivityResultContracts.TakePicture(),
        )
        commonActivityResultHandler = SuspendableActivityResultHandler(
            singleActivityHolder = singleActivityHolder,
            contract = ActivityResultContracts.StartActivityForResult(),
        )
    }

    override suspend fun navigateForResult(destination: ExternalDestination): ExternalResult {
        // TODO: Extract camera and gallery logic to separate classes
        return when (destination) {
            ExternalDestination.CAMERA -> takePhoto()
            ExternalDestination.GALLERY -> pickPhoto()
        }
    }

    // TODO: Add permission request
    private suspend fun takePhoto(): ExternalResult {
        val imageUri = activity.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues(),
        ) ?: return ExternalResult.Cancel

        val isPhotoTakenSuccessfully = cameraResultHandler.awaitResultFor(imageUri)

        return if (isPhotoTakenSuccessfully) {
            ExternalResult.Success(imageUri.getRealPath(activity))
        } else {
            ExternalResult.Cancel
        }
    }

    private suspend fun pickPhoto(): ExternalResult {
        val isPermissionGranted = permissionResultHandler.awaitResultFor(Manifest.permission.READ_EXTERNAL_STORAGE)

        if (!isPermissionGranted) return ExternalResult.Cancel

        val intent = Intent().apply {
            action = Intent.ACTION_PICK
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE)
        }

        val activityResult = commonActivityResultHandler.awaitResultFor(intent)

        val uri = activityResult.data?.data

        return if (activityResult.resultCode == Activity.RESULT_OK && uri != null) {
            ExternalResult.Success(uri.getRealPath(activity))
        } else {
            ExternalResult.Cancel
        }
    }
}