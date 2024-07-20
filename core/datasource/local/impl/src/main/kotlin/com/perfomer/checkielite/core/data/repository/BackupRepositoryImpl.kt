package com.perfomer.checkielite.core.data.repository

import android.content.Context
import android.os.Environment
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.file.FileDataSource
import com.perfomer.checkielite.core.data.entity.BackupProgress
import com.perfomer.checkielite.core.data.service.BackupMode
import com.perfomer.checkielite.core.data.service.BackupService
import com.perfomer.checkielite.core.data.util.startForegroundServiceCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import java.io.File

internal class BackupRepositoryImpl(
    private val context: Context,
    private val fileDataSource: FileDataSource,
    private val databaseDataSource: DatabaseDataSource,
) : BackupRepository {

    private val backupFlow: MutableStateFlow<BackupProgress> = MutableStateFlow(BackupProgress.None)

    override val backupState: BackupProgress
        get() = backupFlow.value

    override fun observeBackupState(): Flow<BackupProgress> {
        return backupFlow
    }

    override suspend fun importBackup(fromPath: String) = withContext(Dispatchers.IO) {
        fileDataSource.importBackup(
            backupPath = fromPath,
            databaseTargetUri = databaseDataSource.getDatabaseSourcePath(),
        )
            .map<Float, BackupProgress> { progress -> BackupProgress.InProgress(progress) }
            .onStart { emit(BackupProgress.None) }
            .catch { error -> emit(BackupProgress.Failure(error)) }
            .onCompletion { emit(BackupProgress.Completed) }
            .collect { progress -> backupFlow.emit(progress) }
    }

    override suspend fun exportBackup() = withContext(Dispatchers.IO) {
        val documentsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath
        val targetFolderPath = "$documentsFolder/Checkie"

        File(targetFolderPath).mkdirs()

        fileDataSource.exportBackup(
            databaseUri = databaseDataSource.getDatabaseSourcePath(),
            picturesUri = databaseDataSource.getAllPicturesUri(),
            destinationFolderUri = targetFolderPath,
        )
            .map<Float, BackupProgress> { progress -> BackupProgress.InProgress(progress) }
            .onStart { emit(BackupProgress.None) }
            .catch { error -> emit(BackupProgress.Failure(error)) }
            .onCompletion { emit(BackupProgress.Completed) }
            .collect { progress -> backupFlow.emit(progress) }
    }

    override fun launchImportBackupService(fromPath: String) {
        context.startForegroundServiceCompat(BackupService.createIntent(context, BackupMode.Import(fromPath)))
    }

    override fun launchExportBackupService() {
        context.startForegroundServiceCompat(BackupService.createIntent(context, BackupMode.Export))
    }
}