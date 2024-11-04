package com.perfomer.checkielite.core.entity.backup

sealed class BackupException(msg: String) : RuntimeException(msg) {

    class DatabaseVersionNotSupported(
        val currentDatabaseVersion: Int,
        val importingDatabaseVersion: Int,
    ) : BackupException("Database version in backup is higher than in application.\nImporting database version was `$importingDatabaseVersion`, but max supported version is `$currentDatabaseVersion`.")
}