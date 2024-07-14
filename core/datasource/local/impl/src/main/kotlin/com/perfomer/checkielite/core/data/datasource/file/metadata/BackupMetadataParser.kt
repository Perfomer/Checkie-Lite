package com.perfomer.checkielite.core.data.datasource.file.metadata

internal interface BackupMetadataParser {

    fun parse(metadata: String): BackupMetadata

    fun serialize(metadata: BackupMetadata): String

    companion object {
        internal const val METADATA_FILENAME = "metadata"
    }
}

internal class BackupMetadataParserImpl : BackupMetadataParser {

    override fun parse(metadata: String): BackupMetadata {
        val entriesPairs = metadata.split(";")
        val entries = entriesPairs.associate { entry ->
            val values = entry.split("=")
            values[0] to values[1]
        }

        return BackupMetadata(
            appVersionCode = entries[APP_VERSION]!!.toInt(),
            backupTimestamp = entries[BACKUP_TIMESTAMP]!!,
            backupVersion = entries[BACKUP_VERSION]!!.toInt(),
        )
    }

    override fun serialize(metadata: BackupMetadata): String {
        return mapOf(
            APP_VERSION to metadata.appVersionCode,
            BACKUP_TIMESTAMP to metadata.backupTimestamp,
            BACKUP_VERSION to metadata.backupVersion,
        )
            .entries
            .joinToString(separator = ";") { (key, value) -> "$key=$value" }
    }

    private companion object {
        private const val APP_VERSION = "appVersion"
        private const val BACKUP_VERSION = "backupVersion"
        private const val BACKUP_TIMESTAMP = "backupTimestamp"
    }
}