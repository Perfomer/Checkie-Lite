package com.perfomer.checkielite.navigation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.Companion.isPhotoPickerAvailable
import androidx.appcompat.app.AppCompatActivity
import com.perfomer.checkielite.common.android.SingleActivityHolder
import com.perfomer.checkielite.common.android.SuspendableActivityResultHandler
import com.perfomer.checkielite.common.android.permissions.PermissionHelper
import com.perfomer.checkielite.common.android.util.getRealPath
import com.perfomer.checkielite.core.navigation.api.ExternalDestination
import com.perfomer.checkielite.core.navigation.api.ExternalResult
import com.perfomer.checkielite.core.navigation.api.ExternalRouter

internal class AndroidExternalRouter(
    private val singleActivityHolder: SingleActivityHolder,
    private val permissionHelper: PermissionHelper,
) : ExternalRouter {

    private val activity: AppCompatActivity
        get() = singleActivityHolder.activity

    private lateinit var cameraResultHandler: SuspendableActivityResultHandler<Uri, Boolean>
    private lateinit var photoPickerResultHandler: SuspendableActivityResultHandler<PickVisualMediaRequest, List<Uri>>
    private lateinit var commonActivityResultHandler: SuspendableActivityResultHandler<Intent, ActivityResult>

    fun register() {
        cameraResultHandler = SuspendableActivityResultHandler(
            singleActivityHolder = singleActivityHolder,
            contract = ActivityResultContracts.TakePicture(),
        )
        photoPickerResultHandler = SuspendableActivityResultHandler(
            singleActivityHolder = singleActivityHolder,
            contract = ActivityResultContracts.PickMultipleVisualMedia(),
        )
        commonActivityResultHandler = SuspendableActivityResultHandler(
            singleActivityHolder = singleActivityHolder,
            contract = ActivityResultContracts.StartActivityForResult(),
        )
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> navigateForResult(destination: ExternalDestination): ExternalResult<T> {
        // TODO: Extract camera and gallery logic to separate classes
        return when (destination) {
            ExternalDestination.GALLERY -> pickPhoto()
            ExternalDestination.FILE -> pickFile()
        } as ExternalResult<T>
    }

    private suspend fun pickPhoto(): ExternalResult<*> {
        val uris = if (isPhotoPickerAvailable(activity)) {
            pickPhotoViaPhotoPicker()
        } else {
            pickPhotoViaFileSystem()
        }

        return if (uris.isNotEmpty()) {
            val realUris = uris.map { uri -> uri.getRealPath(activity) }
            ExternalResult.Success(realUris)
        } else {
            ExternalResult.Cancel
        }
    }

    private suspend fun pickPhotoViaPhotoPicker(): List<Uri> {
        return photoPickerResultHandler.awaitResultFor(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    private suspend fun pickPhotoViaFileSystem(): List<Uri> {
        val isPermissionGranted = permissionHelper.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (!isPermissionGranted) return emptyList()

        val intent = Intent().apply {
            action = Intent.ACTION_PICK
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE)
        }

        val activityResult = commonActivityResultHandler.awaitResultFor(intent)

        val uri = activityResult.data?.data.takeIf { activityResult.resultCode == Activity.RESULT_OK }

        return listOfNotNull(uri)
    }

    private suspend fun pickFile(): ExternalResult<*> {
        val isPermissionGranted = permissionHelper.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (!isPermissionGranted) return ExternalResult.Cancel

        val intent = Intent().apply {
            action = Intent.ACTION_PICK
            setType("*/*")
        }

        val activityResult = commonActivityResultHandler.awaitResultFor(intent)

        val uri = activityResult.data?.data.takeIf { activityResult.resultCode == Activity.RESULT_OK }

        return if (uri != null) ExternalResult.Success(uri) else ExternalResult.Cancel
    }

    private companion object {
        private const val IMAGE_TYPE: String = "image/*"
    }
}