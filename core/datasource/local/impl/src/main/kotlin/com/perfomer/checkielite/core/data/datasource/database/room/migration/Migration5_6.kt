package com.perfomer.checkielite.core.data.datasource.database.room.migration

import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val SLASH_LENGTH = 1
private const val SQLITE_INDEX_OFFSET = 1

/**
 * Replaces absolute paths of review pictures with relative paths.
 */
val MIGRATION_5_6: Migration = object : Migration(5, 6), KoinComponent {

    private val context: Context by inject()

    override fun migrate(db: SupportSQLiteDatabase) {
        val prefixLength = context.filesDir.absolutePath.length + SLASH_LENGTH + SQLITE_INDEX_OFFSET

        db.execSQL(
            """
                UPDATE CheckieReviewPictureDb
                SET uri = SUBSTR(uri, $prefixLength);
            """
        )
    }
}