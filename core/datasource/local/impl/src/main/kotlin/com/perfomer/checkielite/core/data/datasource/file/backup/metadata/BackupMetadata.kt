package com.perfomer.checkielite.core.data.datasource.file.backup.metadata

internal class BackupMetadata(
    val backupVersion: Int,
    val backupTimestamp: String,
    val appVersionCode: Int,
    val databaseVersion: Int?,
)