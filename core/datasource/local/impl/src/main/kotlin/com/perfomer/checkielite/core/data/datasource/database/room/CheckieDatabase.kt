package com.perfomer.checkielite.core.data.datasource.database.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.perfomer.checkielite.core.data.datasource.database.room.converter.DateConverter
import com.perfomer.checkielite.core.data.datasource.database.room.dao.CheckieReviewDao
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewPictureDb

@Database(
    version = 3,
    entities = [
        CheckieReviewDb::class,
        CheckieReviewPictureDb::class,
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
    ]
)
@TypeConverters(DateConverter::class)
internal abstract class CheckieDatabase : RoomDatabase() {

    abstract fun reviewDao(): CheckieReviewDao

    companion object {

        @Synchronized
        internal fun getInstance(
            appContext: Context,
            databaseName: String,
            inMemory: Boolean = false,
        ): CheckieDatabase {
            return if (inMemory) {
                Room.inMemoryDatabaseBuilder(appContext, CheckieDatabase::class.java).build()
            } else {
                Room.databaseBuilder(appContext, CheckieDatabase::class.java, databaseName)
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }

    }
}