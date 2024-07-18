package com.perfomer.checkielite.core.data.datasource.file

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.perfomer.checkielite.common.pure.appInfo.AppInfoProvider
import com.perfomer.checkielite.common.pure.util.randomUuid
import com.perfomer.checkielite.core.data.datasource.database.room.CheckieDatabase
import com.perfomer.checkielite.core.data.datasource.file.backup.BackupProgressNotifier
import com.perfomer.checkielite.core.data.datasource.file.backup.metadata.BackupMetadata
import com.perfomer.checkielite.core.data.datasource.file.backup.metadata.BackupMetadataParser
import com.perfomer.checkielite.core.data.entity.BackupProgress
import com.perfomer.checkielite.core.data.util.archive
import com.perfomer.checkielite.core.data.util.deleteRecursivelyIf
import com.perfomer.checkielite.core.data.util.unarchive
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
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

    suspend fun exportBackup(
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
    private val context: Context,
    private val appInfoProvider: AppInfoProvider,
    private val backupMetadataParser: BackupMetadataParser,
    private val backupProgressNotifier: BackupProgressNotifier,
) : FileDataSource {

    @Suppress("DEPRECATION")
    override suspend fun cacheCompressedPicture(
        uri: String,
        compressTargetSizeBytes: Long,
    ): String {
        val sourceFile = File(uri)
        val destinationFile = File(context.filesDir, randomUuid() + ".webp")

        Compressor.compress(context, sourceFile, Dispatchers.IO) {
            format(Bitmap.CompressFormat.WEBP)
            destination(destinationFile)
            size(compressTargetSizeBytes)
        }

        return destinationFile.absolutePath
    }

    override suspend fun deleteFile(uri: String): Unit = withContext(Dispatchers.IO) {
        File(uri).delete()
    }

    override suspend fun exportBackup(
        databaseUri: String,
        picturesUri: List<String>,
        destinationFolderUri: String
    ) = withContext(Dispatchers.IO) {
        val formatter = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
        val timestamp = formatter.format(Date())

        val fileName = "$timestamp.backup"

        val metadata = BackupMetadata(
            backupVersion = CURRENT_BACKUP_VERSION,
            backupTimestamp = timestamp,
            appVersionCode = appInfoProvider.getAppInfo().versionCode,
        )

        archive(
            files = picturesUri.map(::File) + File(databaseUri),
            destination = File("$destinationFolderUri/$fileName"),
            metadata = backupMetadataParser.serialize(metadata),
        )
            .onStart { backupProgressNotifier.notify(BackupProgress.None) }
            .catch { error -> backupProgressNotifier.notify(BackupProgress.Failure(error)) }
            .collect { progress -> backupProgressNotifier.notify(BackupProgress.Progress(progress)) }
    }

    override suspend fun importBackup(
        backupPath: String,
        databaseTargetUri: String,
    ) = withContext(Dispatchers.IO) {
        val picturesDestinationFolder = context.filesDir

        picturesDestinationFolder.deleteRecursivelyIf { file -> file.extension == "webp" }
        picturesDestinationFolder.mkdirs()

        unarchive(
            context = context,
            zipFile = Uri.parse(backupPath),
            destinationResolver = { fileName ->
                when (fileName) {
                    CheckieDatabase.DATABASE_NAME -> File(databaseTargetUri)
                    BackupMetadataParser.METADATA_FILENAME -> null
                    else -> File(picturesDestinationFolder, fileName)
                }
            }
        )
            .onStart { backupProgressNotifier.notify(BackupProgress.None) }
            .catch { error -> backupProgressNotifier.notify(BackupProgress.Failure(error)) }
            .onCompletion { backupProgressNotifier.notify(BackupProgress.Success) }
            .collect { progress -> backupProgressNotifier.notify(BackupProgress.Progress(progress)) }
    }

    private companion object {
        private const val CURRENT_BACKUP_VERSION = 1
    }
}