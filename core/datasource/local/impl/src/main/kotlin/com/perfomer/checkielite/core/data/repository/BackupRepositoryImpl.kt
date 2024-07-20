package com.perfomer.checkielite.core.data.repository

import android.content.Context
import android.os.Environment
import com.perfomer.checkielite.common.pure.util.runSuspendCatching
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.file.FileDataSource
import com.perfomer.checkielite.core.data.service.BackupParams
import com.perfomer.checkielite.core.data.service.BackupService
import com.perfomer.checkielite.core.data.util.startForegroundServiceCompat
import com.perfomer.checkielite.core.entity.backup.BackupMode
import com.perfomer.checkielite.core.entity.backup.BackupProgress
import com.perfomer.checkielite.core.entity.backup.BackupState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.io.File

internal class BackupRepositoryImpl(
    private val context: Context,
    private val fileDataSource: FileDataSource,
    private val databaseDataSource: DatabaseDataSource,
) : BackupRepository {

    private val backupFlow: MutableStateFlow<BackupState> = MutableStateFlow(BackupState.default)

    override val backupState: BackupState
        get() = backupFlow.value

    override fun observeBackupState(): Flow<BackupState> {
        return backupFlow
    }

    override suspend fun importBackup(fromPath: String) = withContext(Dispatchers.IO) {
        suspend fun updateState(progress: BackupProgress) {
            backupFlow.emit(BackupState(BackupMode.IMPORT, progress))
        }

        runSuspendCatching {
            fileDataSource.importBackup(
                backupPath = fromPath,
                databaseTargetUri = databaseDataSource.getDatabaseSourcePath(),
            ).collect { progress ->
                updateState(BackupProgress.InProgress(progress))
            }
        }
            .onFailure { error -> updateState(BackupProgress.Failure(error)) }
            .onSuccess { updateState(BackupProgress.Completed) }

        updateState(BackupProgress.None)
    }

    override suspend fun exportBackup() = withContext(Dispatchers.IO) {
        suspend fun updateState(progress: BackupProgress) {
            backupFlow.emit(BackupState(BackupMode.EXPORT, progress))
        }

        val documentsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath
        val targetFolderPath = "$documentsFolder/$TARGET_FOLDER_NAME"

        File(targetFolderPath).mkdirs()

        runSuspendCatching {
            fileDataSource.exportBackup(
                databaseUri = databaseDataSource.getDatabaseSourcePath(),
                picturesUri = databaseDataSource.getAllPicturesUri(),
                destinationFolderUri = targetFolderPath,
            ).collect { progress ->
                updateState(BackupProgress.InProgress(progress))
            }
        }
            .onFailure { error -> updateState(BackupProgress.Failure(error)) }
            .onSuccess { updateState(BackupProgress.Completed) }

        updateState(BackupProgress.None)
    }

    override fun launchImportBackupService(fromPath: String) {
        context.startForegroundServiceCompat(BackupService.createIntent(context, BackupParams.Import(fromPath)))
    }

    override fun launchExportBackupService() {
        context.startForegroundServiceCompat(BackupService.createIntent(context, BackupParams.Export))
    }

    private companion object {
        private const val TARGET_FOLDER_NAME: String = "Checkie"
    }
}