package com.perfomer.checkielite.core.data.datasource.file

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.perfomer.checkielite.common.pure.util.randomUuid
import com.perfomer.checkielite.core.data.datasource.database.room.CheckieDatabase
import com.perfomer.checkielite.core.data.util.archive
import com.perfomer.checkielite.core.data.util.unarchive
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal interface FileDataSource {

    suspend fun cacheCompressedPicture(
        uri: String,
        compressTargetSizeBytes: Long = COMPRESS_TARGET_SIZE,
    ): String

    suspend fun deleteFile(uri: String)

    suspend fun createBackup(
        databaseUri: String,
        picturesUri: List<String>,
        destinationFolderUri: String,
    )

    suspend fun importBackup(
        backupPath: String,
        databaseTargetUri: String,
    )

    private companion object {
        const val COMPRESS_TARGET_SIZE = 1 * 1024 * 1024L // 1 MB in bytes
    }
}

internal class FileDataSourceImpl(
    private val applicationContext: Context,
) : FileDataSource {

    @Suppress("DEPRECATION")
    override suspend fun cacheCompressedPicture(
        uri: String,
        compressTargetSizeBytes: Long,
    ): String {
        val sourceFile = File(uri)
        val destinationFile = File(applicationContext.filesDir, randomUuid() + ".webp")

        Compressor.compress(applicationContext, sourceFile, Dispatchers.IO) {
            format(Bitmap.CompressFormat.WEBP)
            destination(destinationFile)
            size(compressTargetSizeBytes)
        }

        return destinationFile.absolutePath
    }

    override suspend fun deleteFile(uri: String): Unit = withContext(Dispatchers.IO) {
        File(uri).delete()
    }

    override suspend fun createBackup(databaseUri: String, picturesUri: List<String>, destinationFolderUri: String) = withContext(Dispatchers.IO) {
        val formatter = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
        val timestamp = formatter.format(Date())

        val fileName = "$timestamp.zip"

        archive(
            files = picturesUri.map(::File) + File(databaseUri),
            destination = File("$destinationFolderUri/$fileName"),
        )
    }

    override suspend fun importBackup(
        backupPath: String,
        databaseTargetUri: String,
    ) = withContext(Dispatchers.IO) {
        val picturesDestinationUri = applicationContext.filesDir

        picturesDestinationUri.deleteRecursively()
        picturesDestinationUri.mkdirs()

        unarchive(
            context = applicationContext,
            zipFile = Uri.parse(backupPath),
            destinationResolver = { fileName ->
                when {
                    fileName == CheckieDatabase.DATABASE_NAME -> File(databaseTargetUri)
                    else -> File(picturesDestinationUri, fileName)
                }
            }
        )
    }
}