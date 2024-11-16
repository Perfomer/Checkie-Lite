package com.perfomer.checkielite.navigation

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.Companion.isPhotoPickerAvailable
import androidx.appcompat.app.AppCompatActivity
import com.perfomer.checkielite.common.android.SingleActivityHolder
import com.perfomer.checkielite.common.android.SuspendableActivityResultHandler
import com.perfomer.checkielite.common.android.permissions.PermissionHelper
import com.perfomer.checkielite.common.android.util.getRealPath
import com.perfomer.checkielite.core.navigation.ExternalDestination
import com.perfomer.checkielite.core.navigation.ExternalResult
import com.perfomer.checkielite.core.navigation.ExternalRouter

internal class AndroidExternalRouter(
    private val singleActivityHolder: SingleActivityHolder,
    private val permissionHelper: PermissionHelper,
) : ExternalRouter {

    private val activity: AppCompatActivity
        get() = singleActivityHolder.activity

    private lateinit var cameraResultHandler: SuspendableActivityResultHandler<Uri, Boolean>
    private lateinit var photoPickerResultHandler: SuspendableActivityResultHandler<PickVisualMediaRequest, List<Uri>>
    private lateinit var filePickerResultHandler: SuspendableActivityResultHandler<Array<String>, Uri?>
    private lateinit var commonActivityResultHandler: SuspendableActivityResultHandler<Intent, ActivityResult>

    fun register() {
        cameraResultHandler = ActivityResultContracts.TakePicture().suspendableResultHandler()
        photoPickerResultHandler = ActivityResultContracts.PickMultipleVisualMedia().suspendableResultHandler()
        filePickerResultHandler = ActivityResultContracts.OpenDocument().suspendableResultHandler()
        commonActivityResultHandler = ActivityResultContracts.StartActivityForResult().suspendableResultHandler()
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> navigateForResult(destination: ExternalDestination): ExternalResult<T> {
        // TODO: Extract camera and gallery logic to separate classes
        return when (destination) {
            ExternalDestination.GALLERY -> pickPhoto()
            ExternalDestination.FILE -> pickFile()
            ExternalDestination.CAMERA -> takePhoto()
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
        val uri = filePickerResultHandler.awaitResultFor(arrayOf("application/octet-stream"))

        if (uri != null) {
            activity.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )

            return ExternalResult.Success(uri.toString())
        }

        return ExternalResult.Cancel
    }

    private suspend fun takePhoto(): ExternalResult<*> {
        val isPermissionGranted = permissionHelper.requestPermission(Manifest.permission.CAMERA)
        if (!isPermissionGranted) return ExternalResult.Cancel

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

    private fun <I, O> ActivityResultContract<I, O>.suspendableResultHandler(): SuspendableActivityResultHandler<I, O> {
        return SuspendableActivityResultHandler(
            singleActivityHolder = singleActivityHolder,
            contract = this,
        )
    }

    private companion object {
        private const val IMAGE_TYPE: String = "image/*"
    }
}