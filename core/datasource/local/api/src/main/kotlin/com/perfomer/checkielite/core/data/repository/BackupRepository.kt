package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.core.domain.entity.backup.BackupState
import kotlinx.coroutines.flow.Flow

interface BackupRepository {

    val backupState: BackupState

    fun observeBackupState(): Flow<BackupState>

    suspend fun importBackup(fromPath: String)

    suspend fun exportBackup()

    suspend fun cancelBackup()

    fun launchImportBackupService(fromPath: String)

    fun launchExportBackupService()
}