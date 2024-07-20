package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.core.data.entity.BackupProgress
import kotlinx.coroutines.flow.Flow

interface BackupRepository {

    val backupState: BackupProgress

    fun observeBackupState(): Flow<BackupProgress>

    suspend fun importBackup(fromPath: String)

    suspend fun exportBackup()

    fun launchImportBackupService(fromPath: String)

    fun launchExportBackupService()
}