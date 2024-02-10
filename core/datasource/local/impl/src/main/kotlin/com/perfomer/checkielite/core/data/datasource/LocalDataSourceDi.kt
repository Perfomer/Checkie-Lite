package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSourceImpl
import com.perfomer.checkielite.core.data.datasource.database.room.CheckieDatabase
import com.perfomer.checkielite.core.data.datasource.file.FileDataSource
import com.perfomer.checkielite.core.data.datasource.file.FileDataSourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val localDataSourceModule = module {
    singleOf(::CheckieLocalDataSourceImpl) bind CheckieLocalDataSource::class

    singleOf(::FileDataSourceImpl) bind FileDataSource::class
    singleOf(::DatabaseDataSourceImpl) bind DatabaseDataSource::class

    single {
        CheckieDatabase.getInstance(
            appContext = androidContext(),
            databaseName = "CheckieDatabase",
            inMemory = false,
        )
    }
}