package com.perfomer.checkielite.core.data.repository

import android.content.Context
import android.os.Environment
import android.util.Log
import com.perfomer.checkielite.common.pure.util.getFormattedDate
import com.perfomer.checkielite.common.pure.util.tryLaunch
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.file.FileDataSource
import com.perfomer.checkielite.core.data.service.BackupParams
import com.perfomer.checkielite.core.data.service.BackupService
import com.perfomer.checkielite.core.data.util.startForegroundServiceCompat
import com.perfomer.checkielite.core.entity.PictureSource
import com.perfomer.checkielite.core.entity.backup.BackupMode
import com.perfomer.checkielite.core.entity.backup.BackupProgress
import com.perfomer.checkielite.core.entity.backup.BackupState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

internal class BackupRepositoryImpl(
    private val context: Context,
    private val fileDataSource: FileDataSource,
    private val databaseDataSource: DatabaseDataSource,
) : BackupRepository {

    private val backupFlow: MutableStateFlow<BackupState> = MutableStateFlow(BackupState.default)

    override val backupState: BackupState
        get() = backupFlow.value

    private var backupJob: Job? = null
    private val backupMutex: Mutex = Mutex()

    override fun observeBackupState(): Flow<BackupState> {
        return backupFlow
    }

    override suspend fun importBackup(fromPath: String) = backupMutex.withLock {
        withContext(Dispatchers.IO) {
            suspend fun updateState(progress: BackupProgress) {
                backupFlow.emit(BackupState(BackupMode.IMPORT, progress))
            }

            backupJob = tryLaunch(
                onSuccess = {
                    updateState(BackupProgress.Completed)
                },
                onError = { throwable ->
                    Log.e(TAG, "Failed to import backup", throwable)
                    updateState(BackupProgress.Failure(throwable))
                },
                onCancel = {
                    updateState(BackupProgress.Cancelled)
                },
                finally = {
                    delay(500L)
                    updateState(BackupProgress.None)
                },
            ) {
                fileDataSource.importBackup(
                    backupPath = fromPath,
                    databaseTempUri = "${context.cacheDir}/$TEMP_DATABASE_FILENAME",
                    databaseTargetUri = databaseDataSource.getDatabaseSourcePath(),
                    databaseVersion = databaseDataSource.getDatabaseVersion(),
                ).collect { progress ->
                    ensureActive()
                    updateState(BackupProgress.InProgress(progress))
                }
            }
        }
    }

    override suspend fun exportBackup() = backupMutex.withLock {
        withContext(Dispatchers.IO) {
            val documentsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath
            val targetFolderPath = "$documentsFolder/$TARGET_FOLDER_NAME"
            val fileName = "${getFormattedDate()}.backup"

            suspend fun updateState(progress: BackupProgress) {
                backupFlow.emit(BackupState(BackupMode.EXPORT, progress))
            }

            backupJob = tryLaunch(
                onSuccess = {
                    updateState(BackupProgress.Completed)
                },
                onError = { throwable ->
                    Log.e(TAG, "Failed to export backup", throwable)
                    updateState(BackupProgress.Failure(throwable))
                },
                onCancel = {
                    updateState(BackupProgress.Cancelled)
                },
                finally = {
                    delay(500L)
                    updateState(BackupProgress.None)
                }
            ) {
                fileDataSource.exportBackup(
                    databaseUri = databaseDataSource.getDatabaseSourcePath(),
                    databaseVersion = databaseDataSource.getDatabaseVersion(),
                    picturesUri = databaseDataSource.getAllPictures()
                        .filter { it.source == PictureSource.APP }
                        .map { it.uri },
                    destinationFolderUri = targetFolderPath,
                    fileName = fileName,
                ).collect { progress ->
                    ensureActive()
                    updateState(BackupProgress.InProgress(progress))
                }
            }
        }
    }

    override suspend fun cancelBackup() {
        backupJob?.cancel()
    }

    override fun launchImportBackupService(fromPath: String) {
        context.startForegroundServiceCompat(BackupService.createIntent(context, BackupParams.Import(fromPath)))
    }

    override fun launchExportBackupService() {
        context.startForegroundServiceCompat(BackupService.createIntent(context, BackupParams.Export))
    }

    private companion object {
        private const val TAG: String = "BackupRepositoryImpl"
        private const val TARGET_FOLDER_NAME: String = "Checkie"
        private const val TEMP_DATABASE_FILENAME: String = "temp_database.db"
    }
}