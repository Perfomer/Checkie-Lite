package com.perfomer.checkielite.core.data.datasource.database.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.perfomer.checkielite.core.data.datasource.database.room.converter.BigDecimalConverter
import com.perfomer.checkielite.core.data.datasource.database.room.converter.DateConverter
import com.perfomer.checkielite.core.data.datasource.database.room.dao.CheckiePictureDao
import com.perfomer.checkielite.core.data.datasource.database.room.dao.CheckieReviewDao
import com.perfomer.checkielite.core.data.datasource.database.room.dao.CheckieTagDao
import com.perfomer.checkielite.core.data.datasource.database.room.dao.RecentSearchDao
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewPictureDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieTagDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieTagReviewBoundDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.RecentSearchedReviewDb

@Database(
    version = 5,
    entities = [
        CheckieReviewDb::class,
        CheckieReviewPictureDb::class,
        CheckieTagDb::class,
        CheckieTagReviewBoundDb::class,
        RecentSearchedReviewDb::class,
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
    ],
)
@TypeConverters(DateConverter::class, BigDecimalConverter::class)
internal abstract class CheckieDatabase : RoomDatabase() {

    abstract fun reviewDao(): CheckieReviewDao
    abstract fun pictureDao(): CheckiePictureDao
    abstract fun tagDao(): CheckieTagDao
    abstract fun recentSearchDao(): RecentSearchDao

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