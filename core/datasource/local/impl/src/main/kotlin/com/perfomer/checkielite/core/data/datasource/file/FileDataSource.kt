package com.perfomer.checkielite.core.data.datasource.file

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.perfomer.checkielite.common.pure.appInfo.AppInfoProvider
import com.perfomer.checkielite.common.pure.util.getFormattedDate
import com.perfomer.checkielite.common.pure.util.randomUuid
import com.perfomer.checkielite.core.data.datasource.database.room.CheckieDatabase
import com.perfomer.checkielite.core.data.datasource.file.backup.metadata.BackupMetadata
import com.perfomer.checkielite.core.data.datasource.file.backup.metadata.BackupMetadataParser
import com.perfomer.checkielite.core.data.util.archive
import com.perfomer.checkielite.core.data.util.unarchive
import com.perfomer.checkielite.core.entity.backup.BackupException
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.withContext
import java.io.File

internal interface FileDataSource {

    suspend fun cacheCompressedPicture(
        uri: String,
        compressTargetSizeBytes: Long = COMPRESS_TARGET_SIZE,
    ): String

    suspend fun clearCompressorCache()

    suspend fun deleteFile(uri: String)

    fun exportBackup(
        databaseUri: String,
        databaseVersion: Int,
        picturesUri: List<String>,
        destinationFolderUri: String,
        fileName: String,
    ): Flow<Float>

    fun importBackup(
        backupPath: String,
        databaseTempUri: String,
        databaseTargetUri: String,
        databaseVersion: Int,
    ): Flow<Float>

    private companion object {
        const val COMPRESS_TARGET_SIZE = 1 * 1024 * 1024L // 1 MB in bytes
    }
}

internal class FileDataSourceImpl(
    private val context: Context,
    private val appInfoProvider: AppInfoProvider,
    private val backupMetadataParser: BackupMetadataParser,
) : FileDataSource {

    @Suppress("DEPRECATION")
    override suspend fun cacheCompressedPicture(
        uri: String,
        compressTargetSizeBytes: Long,
    ): String {
        val sourceFile = File(uri)
        val destinationFile = File(context.filesDir, randomUuid() + ".webp")

        Compressor.compress(context, sourceFile, Dispatchers.Default) {
            format(Bitmap.CompressFormat.WEBP)
            destination(destinationFile)
            size(compressTargetSizeBytes)
        }

        return destinationFile.name
    }

    override suspend fun clearCompressorCache() {
        deleteFile("${context.cacheDir}/compressor")
    }

    override suspend fun deleteFile(uri: String): Unit = withContext(Dispatchers.IO) {
        File(uri).deleteRecursively()
    }

    override fun exportBackup(
        databaseUri: String,
        databaseVersion: Int,
        picturesUri: List<String>,
        destinationFolderUri: String,
        fileName: String,
    ): Flow<Float> {
        val metadata = BackupMetadata(
            backupVersion = CURRENT_BACKUP_VERSION,
            backupTimestamp = getFormattedDate(),
            appVersionCode = appInfoProvider.getAppInfo().versionCode,
            databaseVersion = databaseVersion,
        )

        val databaseFile = File(databaseUri)
        if (!databaseFile.exists()) return flow { throw IllegalStateException("Database doesn't exist") }

        val picturesFiles = picturesUri.map(::File)
            .filter(File::exists)

        return archive(
            files = picturesFiles + databaseFile,
            destination = File("$destinationFolderUri/$fileName"),
            metadata = backupMetadataParser.serialize(metadata),
        )
    }

    override fun importBackup(
        backupPath: String,
        databaseTempUri: String,
        databaseTargetUri: String,
        databaseVersion: Int,
    ): Flow<Float> {
        val picturesDestinationFolder = context.filesDir

        // Pictures we have before importing the backup
        val oldPictures = picturesDestinationFolder.listFiles()
            .orEmpty()
            .filter { it.extension == "webp" }
            .toSet()

        // Pictures we need to delete after backup
        val oldPicturesToDelete = oldPictures.toMutableSet()

        val databaseTemp = File(databaseTempUri)

        return unarchive(
            context = context,
            zipFile = Uri.parse(backupPath),
            metadataParser = { metadataString ->
                verifyMetadata(
                    metadata = backupMetadataParser.parse(metadataString),
                    currentDatabaseVersion = databaseVersion,
                )
            },
            destinationResolver = { fileName ->
                when (fileName) {
                    CheckieDatabase.DATABASE_NAME -> databaseTemp
                    BackupMetadataParser.METADATA_FILENAME -> null
                    else -> File(picturesDestinationFolder, fileName)
                }
            },
        )
            .onCompletion { throwable ->
                if (throwable == null) {
                    finishBackupImport(
                        databaseTemp = databaseTemp,
                        databaseTargetUri = databaseTargetUri,
                        oldPictures = oldPicturesToDelete,
                        picturesDestinationFolder = picturesDestinationFolder,
                    )
                } else {
                    rollbackBackupImport(
                        databaseTemp = databaseTemp,
                        oldPictures = oldPictures,
                        picturesDestinationFolder = picturesDestinationFolder,
                    )
                }
            }
    }

    private suspend fun finishBackupImport(
        databaseTemp: File,
        databaseTargetUri: String,
        oldPictures: Set<File>,
        picturesDestinationFolder: File,
    ) = withContext(Dispatchers.IO) {
        // Move temp database file to the target path
        databaseTemp.renameTo(File(databaseTargetUri))

        // Delete old pictures
        oldPictures.forEach(File::delete)
        picturesDestinationFolder.mkdirs()
    }

    private suspend fun rollbackBackupImport(
        databaseTemp: File,
        oldPictures: Set<File>,
        picturesDestinationFolder: File,
    ) = withContext(Dispatchers.IO) {
        // Delete temporary database file
        databaseTemp.delete()

        // Delete new pictures
        picturesDestinationFolder.listFiles()
            .orEmpty()
            .filter { it.extension == "webp" }
            .filterNot(oldPictures::contains)
            .forEach(File::delete)
    }

    private fun verifyMetadata(
        currentDatabaseVersion: Int,
        metadata: BackupMetadata,
    ) {
        if (metadata.databaseVersion != null && metadata.databaseVersion > currentDatabaseVersion) {
            throw BackupException.DatabaseVersionNotSupported(
                currentDatabaseVersion = currentDatabaseVersion,
                importingDatabaseVersion = metadata.databaseVersion,
            )
        }
    }

    private companion object {
        private const val CURRENT_BACKUP_VERSION = 2
    }
}